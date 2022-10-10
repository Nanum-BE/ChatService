package com.nanum.webfluxservice.chat.infrastructure;

import com.nanum.webfluxservice.alert.domain.Alert;
import com.nanum.webfluxservice.chat.domain.Chat;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface ChatRepositoryCustom {

    Flux<Chat> findAllByRoomIds(String roomId);
    public Mono<Long> countAllReadByUsers(Long userId);
}
