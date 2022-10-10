package com.nanum.webfluxservice.chat.utils;

import com.nanum.webfluxservice.chat.domain.Chat;
import com.nanum.webfluxservice.chat.infrastructure.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RMapReactive;
import org.redisson.api.RedissonReactiveClient;
import org.redisson.codec.TypedJsonJacksonCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service

public class ChatCacheTemplate extends CacheTemplate<String, Chat>{

    @Autowired
    private ChatRepository chatRepository;
    private RMapReactive<String, Chat> map;

    public ChatCacheTemplate(RedissonReactiveClient client){
        this.map = client.getMap("chat", new TypedJsonJacksonCodec(String.class, Chat.class));
    }
    @Override
    protected Mono<Chat> getFromSource(String id) {
        return this.chatRepository.findById(id);
    }

    @Override
    protected Mono<Chat> getFromCache(String id) {
        return this.map.get(id);
    }

    @Override
    protected Mono<Chat> updateSource(String id, Chat chat) {
        return this.chatRepository.findById(id)
                .map(AppUtils::entityToDto)
                .doOnNext(chatDto -> chatDto.setId(id))
                .map(AppUtils::dtoToEntity)
                .flatMap(c->this.chatRepository.save(chat));
    }

    @Override
    protected Mono<Chat> updateCache(String id, Chat chat) {
        return this.map.fastPut(id,chat).thenReturn(chat);
    }

    @Override
    protected Mono<Void> deleteFromSource(String id) {
        return this.chatRepository.deleteById(id);
    }

    @Override
    protected Mono<Void> deleteFromCache(String id) {
        return this.map.fastRemove(id).then();
    }
}
