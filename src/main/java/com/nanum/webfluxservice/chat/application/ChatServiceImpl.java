package com.nanum.webfluxservice.chat.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nanum.exception.RoomNotFoundException;
import com.nanum.webfluxservice.chat.domain.Chat;
import com.nanum.webfluxservice.chat.domain.Room;
import com.nanum.webfluxservice.chat.domain.RoomInfo;
import com.nanum.webfluxservice.chat.domain.UserInfo;
import com.nanum.webfluxservice.chat.dto.ChatDto;
import com.nanum.webfluxservice.chat.infrastructure.ChatRepository;
import com.nanum.webfluxservice.chat.infrastructure.RoomRepository;
import com.nanum.webfluxservice.chat.utils.AppUtils;
import com.nanum.webfluxservice.chat.utils.CacheTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService{
    private final ChatRepository chatRepository;
    private final RoomRepository roomRepository;
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
    public Mono<Chat> add(String msg,String roomId) {
        return roomRepository.findById(roomId)
                .flatMap(room ->
                     chatRepository.save(AppUtils.msgToEntity(msg, roomId))
                );
    }
    @Override
    public Mono<Chat> addV2(String msg, String roomId, Map<String, Object> fromJson) {
        return roomRepository.findById(roomId)
                .flatMap(room -> {
                    List<String> users = new ArrayList<>();
                    for (UserInfo userInfo:room.getRoomInfo().getUsers()) {
                        if(!userInfo.isConnect()){
                            users.add(String.valueOf(userInfo.getUserId()));
                        }
                    }
                    return chatRepository.save(AppUtils.msgToEntityV3(msg, roomId, users, fromJson));
                });
    }
    @Override
    public Flux<ChatDto> getChatsByRoomIdAndUserId(String roomId, Long userId) {

        return  roomRepository.findById(roomId)
                .switchIfEmpty(Mono.defer(() -> {
                    log.error("No room found");
                    return Mono.error(new RoomNotFoundException("room not"));
                }))
                .flatMapMany(room -> {
                    LocalDateTime localDateTime = LocalDateTime.now();
                    for (UserInfo userInfo : room.getRoomInfo().getUsers()) {
                        if (userInfo.getUserId().equals(userId)) {
                            localDateTime = userInfo.getCreateAt();
                        }
                    }
                    return chatRepository.findAllByRoomIdAndCreateAtAfter(roomId, localDateTime);
                }).map(AppUtils::entityToDto);

    }

    @Override
    public Mono<Void> inRoomByRoomIdAndUserIdAndUserName(String id,String userId, String username) {
       String msg =  String.format("{\"sender\":\"%d\",\"message\":\"%s님이 들어왔습니다.\",\"senderName\":\"%s\",\"type\":\"IN\",\"createAt\":\"%s\"}"
               ,userId,username,username, LocalDateTime.now());
        Chat chat = Chat.builder()
                .roomId(id)
                .userId(userId)
                .msg(msg)
                .createAt(LocalDateTime.now())
                .delete(false)
                .type("IN")
                .build();
        return chatRepository.save(chat)
                .switchIfEmpty(Mono.defer(() -> {
                    log.error("No room found");
                    return Mono.error(new RoomNotFoundException("room not"));
                })).then();
    }

    @Override
    public Mono<Void> outRoomByUserIdAndRoomId(String id,String userId, String username) {
        String msg =  String.format("{\"sender\":\"%d\",\"message\":\"%s님이 나갔습니다.\",\"senderName\":\"%s\",\"type\":\"OUT\",\"createAt\":\"%s\"}"
                ,userId,username,username, LocalDateTime.now());
        Chat chat = Chat.builder()
                .roomId(id)
                .userId(userId)
                .msg(msg)
                .type("OUT")
                .createAt(LocalDateTime.now())
                .delete(false)
                .build();
        return chatRepository.save(chat).switchIfEmpty(Mono.defer(() -> {
            log.error("No room found");
            return Mono.error(new RoomNotFoundException("room not"));
        })).then();
    }



    public Mono<Chat> getChatById(String id) {
        return cacheTemplate.get(id);
    }

//    @Override
//    public Flux<ChatDto> connectedRoom(Long roomId) {
//        return chatRepository.mFindByRoomNum(roomId).map(AppUtils::entityToDto);
//    }

}
