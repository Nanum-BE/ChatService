package com.nanum.webfluxservice.chat.application;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.nanum.webfluxservice.alert.application.AlertService;
import com.nanum.webfluxservice.alert.dto.AlertDto;
import com.nanum.webfluxservice.alert.vo.AlertRequest;
import com.nanum.webfluxservice.chat.domain.Room;
import com.nanum.webfluxservice.chat.domain.UserInfo;
import com.nanum.webfluxservice.chat.dto.RoomDto;
import com.nanum.webfluxservice.chat.infrastructure.ChatRepository;
import com.nanum.webfluxservice.chat.infrastructure.RoomRepository;
import com.nanum.webfluxservice.chat.utils.AppUtils;
import com.nanum.webfluxservice.chat.vo.RoomResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService{

    private final RoomRepository roomRepository;
    private final ChatRepository chatRepository;
    private final ChatService chatService;
    private final AlertService alertService;
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
    public Mono<Boolean> validChatRoom(List<Long> params) {
        if(params.size()==2){
            String username = String.format("%d, %d",params.get(0),params.get(1));
            return roomRepository.existsByRoomNameAndHouseId(username,0L);
        }
        return Mono.just(false);

    }

    @Override
    public Mono<RoomDto> getRoom(String roomId) {
        return roomRepository.findById(roomId)
                .map(AppUtils::entityToDto);
    }

    @Override
    public Mono<RoomResponse> getRoomByUserIdAndHouseId(List<Long> params, Long houseId) {
        return roomRepository.findAllByRoomInfoUsersUserIdAndHouseId(params.get(0),houseId)
                .collectList()
                .map(rooms -> {
                    for (Room room:rooms) {
                        List<Long> list = params.stream().distinct().collect(Collectors.toList());
                        List<Long> users = new ArrayList<>();
                        for (UserInfo userInfo:room.getRoomInfo().getUsers()) {
                            users.add(userInfo.getUserId());
                        }
                        Collections.sort(users);
                        Collections.sort(list);
                        boolean result = Arrays.equals(users.toArray(),list.toArray());
                        if(result){
                            return AppUtils.entityToVo(room);
                        }
                    }
                    return AppUtils.entityToVo(Room.builder().build());
                });
    }

    @Override
    public Mono<HashMap<String, Integer>> countAllRoomsByReadMark(Long userId) {
        AtomicInteger count = new AtomicInteger(0);
        return roomRepository.findAllByRoomInfoUsersUserId(userId)
                .collectList()
                .map(rooms -> {
                    for (Room room: rooms) {
                        for (UserInfo userInfo: room.getRoomInfo().getUsers()) {
                            if(userId == userInfo.getUserId()){
                                count.addAndGet(userInfo.getReadCount());
                            }
                        }
                    }
                    HashMap<String, Integer> result = new HashMap<>();
                    result.put("count",count.get());
                    return result;
                });
    }


    @Override
    public Flux<RoomDto> getRooms() {
        return roomRepository.findAll().map(AppUtils::entityToDto);
    }

    @Override
    public Flux<RoomDto> getRoomsByUserId(Long userId) {
        return roomRepository.findAllByRoomInfoUsersUserIdOrderByRoomInfoUpdateAtDesc(userId)
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
                .map(room -> {
                    if(room.getRoomInfo().getUsers()==null || room.getRoomInfo().getUsers().size() == 0){
                        roomRepository.deleteById(id).subscribe();
                    }
                    return AppUtils.entityToDto(room);
                });
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
                                .createAt(LocalDateTime.now())
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

    @Override
    public Mono<RoomDto> updateRoomByUserIdV2(String id, Long userId, String username) {
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
                                .createAt(LocalDateTime.now())
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
    public Mono<Void> updatedConnectRoomByIdByUserIdV2(String id, Long userId, String username) {
        log.info("userInfo--------------"+userId+"::::"+id);
        roomRepository.findById(id)
                .map(room -> {
                    log.info("userInfo--------------"+room.getRoomName()+"::::"+id);
                    for (int i = 0;i<room.getRoomInfo().getUsers().size();i++) {
                        if(room.getRoomInfo().getUsers().get(i).getUserId() == userId){
                            UserInfo changeUserInfo = room.getRoomInfo().getUsers().get(i);
                            changeUserInfo.setConnect(true);
                            changeUserInfo.setReadCount(0);
//                            if(!changeUserInfo.isFirstIn()){
//                                chatService.inRoomByRoomIdAndUserIdAndUserName(id,userId,username);
//                                changeUserInfo.setFirstIn(true);
//                            }
                            room.getRoomInfo().getUsers().set(i,changeUserInfo);
                            break;
                        }
                    }
                    return room;
                }).flatMap(roomRepository::save).then().subscribe();
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
        String updateAt = fromJson.get("createAt").toString();
       return roomRepository.findById(id)
                .map(room -> {
                    log.info("userInfo--------------"+room.getRoomName()+"::::"+id);
                    room.getRoomInfo().setLastMessage(message);
                    room.getRoomInfo().setLastSentUserId(sender);
                    room.getRoomInfo().setLastSentUserName(senderName);
                    room.getRoomInfo().setUpdateAt(updateAt);
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

    @Override
    public Mono<Void> updateCountByRoomIdAndMsgAndSendSSE(String id, String msg) {
        JsonObject jsonObject = (JsonObject) JsonParser.parseString(msg);
        Gson gson = new Gson();
        Map<String, Object> map = new HashMap();
        Map<String, Object> fromJson =(Map) gson.fromJson(jsonObject, map.getClass());
        Long sender = Long.valueOf(fromJson.get("sender").toString());
        String senderName = fromJson.get("username").toString();
        String message = fromJson.get("message").toString();
        String updateAt = fromJson.get("createAt").toString();
        String type = fromJson.get("type").toString();
        Mono<AlertDto> alertDtoMono = roomRepository.findById(id)
                .map(room -> {
                    log.info("userInfo--------------" + room.getRoomName() + "::::" + id);
                    room.getRoomInfo().setLastMessage(type=="IMAGE"?"사진을 보냈습니다.":message);
                    room.getRoomInfo().setLastSentUserId(sender);
                    room.getRoomInfo().setLastSentUserName(senderName);
                    room.getRoomInfo().setUpdateAt(updateAt);
                    for (int i = 0; i < room.getRoomInfo().getUsers().size(); i++) {
                        if (!room.getRoomInfo().getUsers().get(i).isConnect()) {
                            UserInfo changeUserInfo = room.getRoomInfo().getUsers().get(i);
                            changeUserInfo.setReadCount(changeUserInfo.getReadCount() + 1);
                            room.getRoomInfo().getUsers().set(i, changeUserInfo);
                        }
                    }
                    return room;
                }).flatMap(roomRepository::save)
                .map(room -> {
                    List<Long> userIds = new ArrayList<>();
                    for (int i = 0; i < room.getRoomInfo().getUsers().size(); i++) {
                        if (!room.getRoomInfo().getUsers().get(i).isConnect()) {
                            userIds.add(room.getRoomInfo().getUsers().get(i).getUserId());
                        }
                    }
                    AlertRequest alertRequest = AlertRequest.builder()
                            .content(String.format("{\"id\":\"%s\",\"lastMessage\":\"%s\",\"date\":\"%s\"}"
                                    ,room.getId(),room.getRoomInfo().getLastMessage(),room.getUpdateAt()))
                            .title("CHAT")
                            .userIds(userIds)
                            .url("http://localhost:3000/chat")
                            .build();
                    AlertDto alertDto = com.nanum.webfluxservice.alert.utils.AppUtils.voToDto(alertRequest);
                    return alertDto;
                });
        return alertService.saveAlert(alertDtoMono).then();
    }
}
