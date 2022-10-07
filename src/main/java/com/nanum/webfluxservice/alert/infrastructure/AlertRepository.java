package com.nanum.webfluxservice.alert.infrastructure;

import com.nanum.webfluxservice.alert.domain.Alert;

import com.nanum.webfluxservice.alert.domain.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import org.springframework.data.mongodb.repository.Tailable;

import org.springframework.stereotype.Repository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
@Repository
public interface AlertRepository extends ReactiveMongoRepository<Alert, String>,AlertRepositoryCustom{
//    Flux<AlertDto> findByPriceBetween(Range<Double> priceRange);


//    @Tailable // 커서를 안닫고 계속 유지한다.
//    @Query(value = "{ 'user_ids' : { $all : [?0] }}")
//    Flux<Alert> mFindByUserId(List<Long> userId);
////    @Tailable
////    Flux<Alert> findByUserIdsInAAndAndReadByUserIdsNotIn(List<Long> userId
////            ,List<Long> readByUserIdsNot);
//    @Tailable
//    Flux<Alert> findByUserIdsInAndCreateAtAfter(List<Long> userId,
//                                                LocalDateTime localDateTime);
//@Update
//    Flux<Alert> findByUserIdsInAndDeletedByUserIdsNotIn(List<Long> userIds,List<Long> deletedUsers);
//    Mono<Long> countByUserIdsInAndDeletedByUserIdsNotInAndReadByUserIdsNotIn(List<Long> userIds
//            ,List<Long> deletedUsers , List<Long> readUsers);
    @Tailable
    Mono<Alert> findByUsersContainsAndCreateAtAfter(List<User> user, LocalDateTime localDateTime);



}
