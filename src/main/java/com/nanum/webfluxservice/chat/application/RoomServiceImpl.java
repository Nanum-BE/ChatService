package com.nanum.webfluxservice.chat.application;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.nanum.webfluxservice.chat.domain.Room;
import com.nanum.webfluxservice.chat.domain.UserInfo;
import com.nanum.webfluxservice.chat.dto.RoomDto;
import com.nanum.webfluxservice.chat.infrastructure.ChatRepository;
import com.nanum.webfluxservice.chat.infrastructure.RoomRepository;
import com.nanum.webfluxservice.chat.utils.AppUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.codec.ServerSentEventHttpMessageReader;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService{

    private final RoomRepository roomRepository;
    private final ChatRepository chatRepository;
    @Override
    public Mono<RoomDto> save(Mono<RoomDto> roomDtoMono) {
        return roomDtoMono.map(AppUtils::dtoToEntity)
                .flatMap(roomRepository::insert)
                .map(AppUtils::entityToDto);
        /*
                return roomDtoMono.flatMap(roomDto -> {
            RoomInfo roomInfo = AppUtils.toEntity(roomDto);
            Mono<RoomDto> dtoMono = roomInfoRepository.insert(roomInfo)
                    .map(roomInfo1 -> {
                        roomDto.setRoomInfoId(roomInfo1.getId());
                        return roomDto;
                    });
        return dtoMono;
        }).map(AppUtils::dtoToEntity)
                .flatMap(roomRepository::insert)
                .map(AppUtils::entityToDto);
         */
    }

    @Override
    public Mono<Void> updatedConnectRoomByIdByUserId(String id, Long userId) {
        log.info("userInfo--------------"+userId+"::::"+id);
         roomRepository.findById(id)
                .map(room -> {
                    log.info("userInfo--------------"+room.getRoomName()+"::::"+id);
                    for (int i = 0;i<room.getRoomInfo().getUsers().size();i++) {
                        if(room.getRoomInfo().getUsers().get(i).getUserId() == userId){
                            UserInfo changeUserInfo = room.getRoomInfo().getUsers().get(i);
                            changeUserInfo.setConnect(true);
                            changeUserInfo.setReadCount(0);
                            room.getRoomInfo().getUsers().set(i,changeUserInfo);
                            break;
                        }
                    }
                    return room;
                }).flatMap(roomRepository::save).then().subscribe();
         return null;
    }

    @Override
    public Mono<Void> cancelConnectRoomByIdByUserId(String id, Long userId) {
        roomRepository.findById(id)
                .map(room -> {
                    log.info("userInfo--------------"+room.getRoomName()+"::::"+id);
                    for (int i = 0;i<room.getRoomInfo().getUsers().size();i++) {
                        if(room.getRoomInfo().getUsers().get(i).getUserId() == userId){
                            UserInfo changeUserInfo = room.getRoomInfo().getUsers().get(i);
                            changeUserInfo.setConnect(false);
                            room.getRoomInfo().getUsers().set(i,changeUserInfo);
                            break;
                        }
                    }
                    return room;
                }).flatMap(roomRepository::save).then().subscribe();

        return null;
    }

    @Override
    public Flux<RoomDto> getRooms() {
        return roomRepository.findAll().map(AppUtils::entityToDto);
    }

    @Override
    public Flux<RoomDto> getRoomsByUserId(Long userId) {
        return roomRepository.findAllByRoomInfoUsersUserIdOrderByUpdateAtDesc(userId)
                .map(AppUtils::entityToDto);
    }

    @Override
    public Mono<RoomDto> deleteRoomByUserId(String id, Long userId) {
        return roomRepository.findById(id)
                .map(AppUtils::entityToDto)
                .map(roomDto -> {
                    for (UserInfo userInfo:roomDto.getRoomInfo().getUsers()) {
                        if(userId==userInfo.getUserId()){
                            UserInfo findUser = userInfo;
                            roomDto.getRoomInfo().getUsers().remove(findUser);
                            break;
                        }
                    }

                    return roomDto;
                }).map(AppUtils::dtoToEntity)
                .flatMap(roomRepository::save)
                .map(AppUtils::entityToDto);
    }

    @Override
    public Mono<RoomDto> updateRoomByUserId(String id, Long userId) {
        return roomRepository.findById(id)
                .map(AppUtils::entityToDto)
                .map(roomDto -> {
                    boolean valid = true;
                    for (UserInfo userInfo:roomDto.getRoomInfo().getUsers()) {
                        if(userId==userInfo.getUserId()){
                            valid = false;
                            break;
                        }
                    }
                    if(valid){
                        UserInfo userInfo = UserInfo.builder().userId(userId)
                                .readCount(0)
                                .connect(false)
                                .build();
                        roomDto.getRoomInfo().getUsers().add(userInfo);
                    }else{
                        log.error(userId+"'s already existed");
                    }
                    return roomDto;
                }).map(AppUtils::dtoToEntity)
                .flatMap(roomRepository::save)
                .map(AppUtils::entityToDto);

    }
    public Flux<Room> findAllBySSE(Long userId) {
        // Simulate the data streaming every 2 seconds.

        return null;
    }
    @Override
    public Mono<Void> updateCountByRoomIdAndMsg(String id,String msg) {
        JsonObject jsonObject = (JsonObject) JsonParser.parseString(msg);
        Gson gson = new Gson();
        Map<String, Object> map = new HashMap();
        Map<String, Object> fromJson =(Map) gson.fromJson(jsonObject, map.getClass());
        Long sender = Long.valueOf(fromJson.get("sender").toString());
        String senderName = fromJson.get("senderName").toString();
        String message = fromJson.get("message").toString();
       return roomRepository.findById(id)
                .map(room -> {
                    log.info("userInfo--------------"+room.getRoomName()+"::::"+id);
                    room.getRoomInfo().setLastMessage(message);
                    room.getRoomInfo().setLastSentUserId(sender);
                    room.getRoomInfo().setLastSentUserName(senderName);
                    for (int i = 0;i<room.getRoomInfo().getUsers().size();i++) {
                        if(!room.getRoomInfo().getUsers().get(i).isConnect()){
                            UserInfo changeUserInfo = room.getRoomInfo().getUsers().get(i);
                            changeUserInfo.setReadCount(changeUserInfo.getReadCount()+1);
                            room.getRoomInfo().getUsers().set(i,changeUserInfo);
                        }
                    }
                    return room;
                }).flatMap(roomRepository::save).then();
    }
}