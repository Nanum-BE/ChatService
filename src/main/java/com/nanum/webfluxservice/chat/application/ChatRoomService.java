package com.nanum.webfluxservice.chat.application;

import com.nanum.webfluxservice.chat.domain.Chat;
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
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatRoomService implements WebSocketHandler {


    private final RedissonReactiveClient client;
    private final ChatService chatService;


    @Override
    public Mono<Void> handle(WebSocketSession session) {

        log.info("init Handle");
        String roomId = getChatRoomName(session);
        Mono<RoomDto> chatRoom = findChatRoom(roomId);
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
                .doFinally(signalType -> log.info("Publisher finally "+signalType));

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

    private Mono<RoomDto> findChatRoom(String roomId) {
        return null;
    }

    private String getChatRoomName(WebSocketSession session){

        log.info("session: "+ session);
        log.info("getHandshakeInfo: "+ session.getHandshakeInfo());
        URI uri = session.getHandshakeInfo().getUri();
        log.info("fromUri: "+ UriComponentsBuilder.fromUri(uri).build());
        String id = UriComponentsBuilder.fromUri(uri)
                .build()
                .getQueryParams()
                .toSingleValueMap()
                .getOrDefault("room", "default");

        return UriComponentsBuilder.fromUri(uri)
                .build()
                .getQueryParams()
                .toSingleValueMap()
                .getOrDefault("room","default");
    }
}
