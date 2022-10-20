package com.nanum.webfluxservice.chat.infrastructure;

import com.mongodb.client.result.UpdateResult;
import com.nanum.webfluxservice.chat.domain.Chat;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface ChatRepositoryCustom {

    Flux<Chat> findAllByRoomIds(String roomId);
    public Mono<Long> countAllReadByUsers(Long userId);

    public Mono<UpdateResult> deleteByUsers(String roomId, String userId);
}
