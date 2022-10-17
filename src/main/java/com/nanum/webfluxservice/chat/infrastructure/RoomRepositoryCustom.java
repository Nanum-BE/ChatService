package com.nanum.webfluxservice.chat.infrastructure;

import com.nanum.webfluxservice.chat.domain.Room;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface RoomRepositoryCustom {
    Flux<Room> findAllByUserId(Long userId);


    Mono<Boolean> existsByRoomInfoUsersUserIdAndHouseId(List<Long> userIds);
}
