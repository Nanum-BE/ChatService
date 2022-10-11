package com.nanum.webfluxservice.chat.infrastructure;

import com.nanum.webfluxservice.chat.domain.Chat;
import org.springframework.data.mongodb.repository.Meta;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Tailable;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
@Repository
public interface ChatRepository extends ReactiveMongoRepository<Chat, String>,ChatRepositoryCustom {

//    @Tailable // 커서를 안닫고 계속 유지한다.
//    @Query("{senderId:?0, receiverId:?1}")
//    Flux<Chat> mFindBySender(Long senderId, Long receiverId);
//
//    @Tailable // 커서를 안닫고 계속 유지한다.
//    @Query("{roomNum:?0}")
//    Flux<Chat> mFindByRoomNum(Long roomId);
//
//    @Tailable
//    Flux<Chat> findAllByDeleteIsFalse();

    Flux<Chat> findAllByRoomId(String roomId);
}
