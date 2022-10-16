package com.nanum.webfluxservice.chat.application;

import com.nanum.webfluxservice.chat.domain.Chat;
import com.nanum.webfluxservice.chat.domain.Room;
import com.nanum.webfluxservice.chat.dto.RoomDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import netscape.javascript.JSObject;
import org.redisson.api.RListReactive;
import org.redisson.api.RTopicReactive;
import org.redisson.api.RedissonReactiveClient;
import org.redisson.client.codec.StringCodec;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.http.websocket.WebsocketInbound;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatRoomService implements WebSocketHandler {


    private final RedissonReactiveClient client;
    private final ChatService chatService;

    private final RoomService roomService;



    @Override
    public Mono<Void> handle(WebSocketSession session) {


            log.info("init Handle");
        HashMap<String, String> uriInfo = getChatRoomName(session);
        String roomId = uriInfo.get("room");
        Long userId = Long.valueOf(uriInfo.get("userId"));
//        String username = uriInfo.get("username");
        startChatRoom(roomId, userId);

        log.info("session.getId()"+session.getId());
        log.info("room: "+ roomId);

        RTopicReactive topic = this.client.getTopic(roomId, StringCodec.INSTANCE);

        // subscribe
        session.receive()
                .map(WebSocketMessage::getPayloadAsText)
                .flatMap(msg-> chatService.add(msg,roomId)
                        .then(roomService.updateCountByRoomIdAndMsgAndSendSSE(roomId,msg))
                        .then(topic.publish(msg)))
                .doOnError(er->log.error(String.valueOf(er)))
                .doFinally(signalType -> log.info("Subscriber finally "+signalType))
                .subscribe();

       // publisher
        Flux<WebSocketMessage> flux = topic.getMessages(String.class)
                .startWith(chatService.getChatsByRoomIdAndUserId(roomId,userId).map(chat -> chat.getMsg()))
                .map(session::textMessage)
                .doOnError(er->log.error(String.valueOf(er)))
                .doFinally(signalType -> {log.info("Publisher finally "+signalType);
                    if(signalType.toString()=="cancel"){
                     roomService.cancelConnectRoomByIdByUserId(roomId,userId);
                    }
                });

        // close
        session.close()
                .doFinally(signalType -> log.info("close finally "+signalType));

        return session.send(flux);
    }

    private Mono<Void> startChatRoom(String roomId, Long userId) {
         roomService.updatedConnectRoomByIdByUserId(roomId,userId);
//        roomService.updatedConnectRoomByIdByUserIdV2(roomId,userId, username);
         return null;
    }

    private HashMap<String,String> getChatRoomName(WebSocketSession session){

        log.info("session: "+ session);
        log.info("getHandshakeInfo: "+ session.getHandshakeInfo());
        URI uri = session.getHandshakeInfo().getUri();
        log.info("fromUri: "+ UriComponentsBuilder.fromUri(uri).build());
        String userId = UriComponentsBuilder.fromUri(uri)
                .build()
                .getQueryParams()
                .toSingleValueMap()
                .getOrDefault("userId", "default");
        log.info("userId: "+userId);
        String roomId = UriComponentsBuilder.fromUri(uri)
                .build()
                .getQueryParams()
                .toSingleValueMap()
                .getOrDefault("room","default");
        HashMap<String, String> result = new HashMap<>();
        result.put("room",roomId);
        result.put("userId",userId);
//
//        String username = UriComponentsBuilder.fromUri(uri)
//                .build()
//                .getQueryParams()
//                .toSingleValueMap()
//                .getOrDefault("username","default");
//
//        result.put("username",username);

        return result;
    }
}

