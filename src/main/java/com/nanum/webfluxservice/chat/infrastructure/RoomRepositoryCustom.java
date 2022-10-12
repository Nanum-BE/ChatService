package com.nanum.webfluxservice.chat.infrastructure;

import com.nanum.webfluxservice.chat.domain.Room;
import reactor.core.publisher.Flux;

public interface RoomRepositoryCustom {
    Flux<Room> findAllByUserId(Long userId);
}
