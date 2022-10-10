package com.nanum.webfluxservice.chat.infrastructure;

import com.nanum.webfluxservice.alert.domain.Alert;
import com.nanum.webfluxservice.alert.infrastructure.AlertRepositoryCustom;
import com.nanum.webfluxservice.chat.domain.Chat;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ChatRepositoryImpl implements ChatRepositoryCustom {
    private final ReactiveMongoTemplate reactiveMongoTemplate;

    public Flux<Chat> findAllByRoomIds(String roomId){
        Query query = new Query();
        query.addCriteria(
                Criteria.where("roomId").is(roomId));

        query.fields().include("msg");

        return reactiveMongoTemplate.find(query, Chat.class);
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
