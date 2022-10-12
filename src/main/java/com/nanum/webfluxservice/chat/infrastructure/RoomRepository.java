package com.nanum.webfluxservice.chat.infrastructure;

import com.nanum.webfluxservice.chat.domain.Room;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

public interface RoomRepository extends ReactiveMongoRepository<Room, String>, RoomRepositoryCustom {

    Flux<Room> findAllByRoomInfoUsersUserIdOrderByUpdateAtDesc(Long userId);
//    Flux<Room> findAllByUserIdsInAn(Long userId);



}
