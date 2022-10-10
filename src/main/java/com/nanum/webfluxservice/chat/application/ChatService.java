package com.nanum.webfluxservice.chat.application;

import com.nanum.webfluxservice.chat.domain.Chat;
import com.nanum.webfluxservice.chat.dto.ChatDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ChatService {

//    Flux<ChatDto> findBySenderAndReceiver(Long senderId, Long receiverId);

    Mono<ChatDto>  chatSave(ChatDto chatDto);

    Flux<Chat> getChatsByRoom(String houseId);

    Mono<Void> add(String msg,String roomId);
//    Flux<ChatDto> connectedRoom(Long roomId);
}
