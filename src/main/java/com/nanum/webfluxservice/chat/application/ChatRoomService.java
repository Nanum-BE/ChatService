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

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@Slf4j
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

        startChatRoom(roomId, userId);

        log.info("session.getId()"+session.getId());
        log.info("room: "+ roomId);

        RTopicReactive topic = this.client.getTopic(roomId, StringCodec.INSTANCE);
        RListReactive<String> list = this.client.getList("history:" + roomId, StringCodec.INSTANCE);
        Flux<Chat> chatsByRoom = chatService.getChatsByRoom(roomId);
        list.isExists().map(e->{
            if(!e){
                chatsByRoom.flatMap(chat -> list.add(chat.getMsg()));
            }
            return e;
        });
        // subscribe
        session.receive()
                .map(WebSocketMessage::getPayloadAsText)
                .flatMap(msg-> chatService.add(msg,roomId).then(list.add(msg))
                        .then(roomService.updateCountByRoomIdAndMsg(roomId,msg))
                        .then(topic.publish(msg)))
                .doOnError(er->log.error(String.valueOf(er)))
                .doFinally(signalType -> log.info("Subscriber finally "+signalType))
                .subscribe();

/*
        원본
        // subscribe
        session.receive()
                .map(WebSocketMessage::getPayloadAsText)
                .flatMap(msg->list.add(msg).then(topic.publish(msg)))
                .doOnError(er->log.error(String.valueOf(er)))
                .doFinally(signalType -> log.info("Subscriber finally "+signalType))
                .subscribe();

 */
//        Flux<WebSocketMessage> mongodbFlux = chatsByRoom.
//                map(chat -> session.textMessage(chat.getMsg()))
//                .doOnError(er -> log.error(String.valueOf(er)))
//                .doFinally(signalType -> log.info("Publisher finally " + signalType));
        session.close()
                .doFinally(signalType -> log.info("close finally "+signalType));
        // publisher
        Flux<WebSocketMessage> flux = topic.getMessages(String.class)
                .startWith(list.iterator())
                .map(session::textMessage)
                .doOnError(er->log.error(String.valueOf(er)))
                .doFinally(signalType -> {log.info("Publisher finally "+signalType);
                    if(signalType.toString()=="cancel"){
                     roomService.cancelConnectRoomByIdByUserId(roomId,userId);
                    }
                });

//        // publisher
//        Flux<WebSocketMessage> flux = topic.getMessages(String.class)
//
//                .startWith(list.iterator())
//                .map(session::textMessage)
//                .doOnError(er->log.error(String.valueOf(er)))
//                .doFinally(signalType -> log.info("Publisher finally "+signalType));

//        session.send();
        return session.send(flux);
    }

    private Mono<Void> startChatRoom(String roomId, Long userId) {
         roomService.updatedConnectRoomByIdByUserId(roomId,userId);
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
        return result;
    }
}
