package com.nanum.webfluxservice.chat.infrastructure;

import com.nanum.webfluxservice.alert.domain.Alert;
import com.nanum.webfluxservice.chat.domain.Room;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
public class RoomRepositoryImpl implements RoomRepositoryCustom {
    private final ReactiveMongoTemplate reactiveMongoTemplate;
    @Override
    public Flux<Room> findAllByUserId(Long userId){
        Query query = new Query();
        query.addCriteria(
                Criteria.where("userIds").in(userId)
                );
        query.fields().include("id");
        query.fields().include("houseId");
        query.fields().include("userIds");
        query.fields().include("roomName");
        query.fields().include("roomInfo");
        query.fields().include("createAt");
        query.fields().include("updateAt");
        query.fields().include("deleteAt");
        return reactiveMongoTemplate.find(query,Room.class);
    }



    @Override
    public Mono<Boolean> existsByRoomInfoUsersUserIdAndHouseId(List<Long> userIds){
        Query query = new Query();
        query.addCriteria(
                Criteria.where("roomInfo").elemMatch(
                                Criteria
                                        .where("users").elemMatch(
                                        Criteria.where("userId").in(userIds)
                                        )
                                ) .and("houseId").is(0L)
        );
        return reactiveMongoTemplate.exists(query,Room.class);
    }

    public Mono<Long> countAllReadByUsers(Long userId){
        Query query = new Query();
        query.addCriteria(
                Criteria.where("roomNum")
                        .and("users").elemMatch(
                        Criteria.where("userId").is(userId)
                                .and("readMark").is(false)
                                .and("delete").is(false)
                ));

        return reactiveMongoTemplate.count(query,Alert.class);
    }
}
