package com.nanum.webfluxservice.chat.infrastructure;

import com.nanum.webfluxservice.chat.domain.Room;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RoomRepository extends ReactiveMongoRepository<Room, String>, RoomRepositoryCustom {

    Flux<Room> findAllByRoomInfoUsersUserId(Long userId);
//    Flux<Room> findAllByUserIdsInAn(Long userId);
    Flux<Room> findAllByRoomInfoUsersUserIdOrderByRoomInfoUpdateAtDesc(Long userId);

    Flux<Room> findAllByRoomInfoUsersUserIdAndHouseId(Long userId,Long houseId);


    Mono<Boolean> existsByRoomNameAndHouseId(String username, Long houseId);


    Mono<Room> findByHouseId(Long houseId);
}
