package com.nanum.webfluxservice.chat.infrastructure;

import com.nanum.webfluxservice.chat.domain.Chat;
import org.springframework.data.mongodb.repository.Meta;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Tailable;
import reactor.core.publisher.Flux;

public interface ChatRepository extends ReactiveMongoRepository<Chat, String> {

    @Tailable // 커서를 안닫고 계속 유지한다.
    @Query("{sender:?0, receiver:?1}")
    @Meta(cursorBatchSize = 1024)
    Flux<Chat> mFindBySender(Long sender, Long receiver);
}
