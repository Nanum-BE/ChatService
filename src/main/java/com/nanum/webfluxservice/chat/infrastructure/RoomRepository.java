package com.nanum.webfluxservice.chat.infrastructure;

import com.nanum.webfluxservice.chat.domain.Room;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RoomRepository extends ReactiveMongoRepository<Room, String>, RoomRepositoryCustom {

    Flux<Room> findAllByUserIdsIn(Long userId);
//    Flux<Room> findAllByUserIdsInAn(Long userId);
}
