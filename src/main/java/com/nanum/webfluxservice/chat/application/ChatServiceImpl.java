package com.nanum.webfluxservice.chat.application;

import com.nanum.webfluxservice.chat.domain.Chat;
import com.nanum.webfluxservice.chat.dto.ChatDto;
import com.nanum.webfluxservice.chat.infrastructure.ChatRepository;
import com.nanum.webfluxservice.chat.utils.AppUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService{
    private final ChatRepository chatRepository;
    @Override
    public Flux<ChatDto> findBySenderAndReceiver(Long senderId, Long receiverId) {
        return chatRepository.mFindBySender(senderId,receiverId).map(AppUtils::entityToDto);
    }

    @Override
    public Mono<ChatDto> chatSave(ChatDto chatDto) {
        Chat chat = AppUtils.dtoToEntity(chatDto);
        return chatRepository.save(chat).map(AppUtils::entityToDto);
    }

}
