package com.nanum.webfluxservice.chat.application;

import com.nanum.webfluxservice.chat.domain.Chat;
import com.nanum.webfluxservice.chat.dto.ChatDto;
import com.nanum.webfluxservice.chat.infrastructure.ChatRepository;
import com.nanum.webfluxservice.chat.utils.AppUtils;
import com.nanum.webfluxservice.chat.utils.CacheTemplate;
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

    private final CacheTemplate<String, Chat> cacheTemplate;
//    @Override
//    public Flux<ChatDto> findBySenderAndReceiver(Long senderId, Long receiverId) {
//        return chatRepository.mFindBySender(senderId,receiverId).map(AppUtils::entityToDto);
//    }

    @Override
    public Mono<ChatDto> chatSave(ChatDto chatDto) {
        Chat chat = AppUtils.dtoToEntity(chatDto);
        return chatRepository.save(chat).map(AppUtils::entityToDto);
    }

    @Override
    public Flux<Chat> getChatsByRoom(String houseId) {
        return chatRepository.findAllByRoomId(houseId);

    }

    @Override
    public Mono<Void> add(String msg,String roomId) {

        return chatRepository.save(AppUtils.msgToEntity(msg,roomId))
                .then();
    }

    public Mono<Chat> getChatById(String id) {
        return cacheTemplate.get(id);
    }

//    @Override
//    public Flux<ChatDto> connectedRoom(Long roomId) {
//        return chatRepository.mFindByRoomNum(roomId).map(AppUtils::entityToDto);
//    }

}
