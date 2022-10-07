package com.nanum.webfluxservice.alert.infrastructure;

import com.nanum.webfluxservice.alert.domain.Alert;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class AlertRepositoryImpl implements AlertRepositoryCustom{
    private final ReactiveMongoTemplate reactiveMongoTemplate;

    public Flux<Alert> findAllReadByUsers(Long userId){
        Query query = new Query();
        query.addCriteria(
                Criteria.where("users").elemMatch(
                        Criteria.where("userId").is(userId)
                                .and("delete").is(false)
                ));
        query.fields().include("id");
        query.fields().include("content");
        query.fields().include("createAt");
        query.fields().include("url");
        query.fields().include("deleteAt");
        query.fields().include("users.$");
        return reactiveMongoTemplate.find(query,Alert.class);
    }

    public Mono<Long> CountAllReadByUsers(Long userId){
        Query query = new Query();
        query.addCriteria(
                Criteria.where("users").elemMatch(
                        Criteria.where("userId").is(userId)
                                .and("readMark").is(false)
                                .and("delete").is(false)
                ));
        query.fields().include("id");
        query.fields().include("content");
        query.fields().include("createAt");
        query.fields().include("url");
        query.fields().include("deleteAt");
        query.fields().include("users.$");
        return reactiveMongoTemplate.count(query,Alert.class);
    }
}
