package com.nanum.webfluxservice.chat.infrastructure;

import com.mongodb.client.result.UpdateResult;
import com.nanum.webfluxservice.chat.domain.Chat;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
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

    @Override
    public Mono<Long> countAllReadByUsers(Long userId) {
        return null;
    }


    @Override
    public Mono<UpdateResult> deleteByUsers(String roomId, String userId) {
        Query query = new Query();
        query.addCriteria(
                Criteria.where("roomId").is(roomId)
                        .and("users").in(userId)
        );
        Update update = new Update();
        update.pull("users",userId);

        return reactiveMongoTemplate.updateMulti(query,update,Chat.class);
    }
}
