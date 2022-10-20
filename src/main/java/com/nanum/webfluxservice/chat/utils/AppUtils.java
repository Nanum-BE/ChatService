package com.nanum.webfluxservice.chat.utils;

import com.google.gson.Gson;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.nanum.webfluxservice.chat.domain.Chat;
import com.nanum.webfluxservice.chat.domain.Room;
import com.nanum.webfluxservice.chat.domain.RoomInfo;
import com.nanum.webfluxservice.chat.domain.UserInfo;
import com.nanum.webfluxservice.chat.dto.ChatDto;
import com.nanum.webfluxservice.chat.dto.RoomDto;
import com.nanum.webfluxservice.chat.vo.RoomRequest;
import com.nanum.webfluxservice.chat.vo.RoomResponse;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
public class AppUtils {
    public static Chat dtoToEntity(ChatDto chatDto){
        
        return Chat.builder()
                .id(chatDto.getId())
                .msg(chatDto.getMsg())
                .userId(chatDto.getUserId())
                .delete(chatDto.isDelete())
                .createAt(chatDto.getCreateAt())
                .roomId(chatDto.getRoomId())
                .updateAt(chatDto.getUpdateAt())
                .users(chatDto.getUsers())
                .build();
    }
    public static Chat msgToEntityV3(String msg, String roomId, List<String> users, Map<String, Object> fromJson) {
        String sender = fromJson.get("sender").toString();
        String type = fromJson.get("type").toString();
        String username = fromJson.get("username").toString();
        String updateAt = fromJson.get("createAt").toString();
        return Chat.builder()
                .msg(msg)
                .userId(sender)
                .delete(false)
                .type(type)
                .username(username)
                .updateAt(updateAt)
                .createAt(null)
                .users(users)
                .roomId(roomId)
                .build();
    }
    public static Chat msgToEntityV2(String msg, String roomId, List<String> users){


        JsonObject jsonObject = (JsonObject) JsonParser.parseString(msg);
        Gson gson = new Gson();
        Map<String, Object> map = new HashMap();
        Map<String, Object> fromJson =(Map) gson.fromJson(jsonObject, map.getClass());
        String sender = fromJson.get("sender").toString();
        String type = fromJson.get("type").toString();
        String username = fromJson.get("username").toString();
        String updateAt = fromJson.get("createAt").toString();
        // 메시지에 count 넣기
//        String[] msgEncode = msg.split("\"}");
//        String msgCreate = "";
//        for (int i = 0; i<msgEncode.length - 1; i++ ) {
//            log.info("msgCreate",msgEncode[i]);
//            msgCreate += msgEncode[i];
//        }
//        msg =String.format("%s\",\"count\":\"%d\"}",msgEncode[0],count);

//        String message =  String.format("{\"message\":%s,\"users\": %s }", msg, gson.toJson(users,List.class));
        return Chat.builder()
                .msg(msg)
                .userId(sender)
                .delete(false)
                .type(type)
                .username(username)
                .updateAt(updateAt)
                .createAt(null)
                .users(users)
                .roomId(roomId)
                .build();

    }
    public static Chat msgToEntity(String msg, String roomId){


        JsonObject jsonObject = (JsonObject) JsonParser.parseString(msg);
        Gson gson = new Gson();
        Map<String, Object> map = new HashMap();
        Map<String, Object> fromJson =(Map) gson.fromJson(jsonObject, map.getClass());
        String sender = fromJson.get("sender").toString();
        String type = fromJson.get("type").toString();
        String username = fromJson.get("username").toString();
        String updateAt = fromJson.get("createAt").toString();
        return Chat.builder()
                    .msg(msg)
                    .userId(sender)
                    .delete(false)
                    .type(type)
                    .username(username)
                    .updateAt(updateAt)
                    .createAt(null)
                    .roomId(roomId)
                    .build();

    }
    public static ChatDto entityToDto(Chat chat){
        return ChatDto.builder()
                .id(chat.getId())
                .msg(chat.getMsg())
                .userId(chat.getUserId())
                .createAt(chat.getCreateAt())
                .delete(chat.isDelete())
                .roomId(chat.getRoomId())
                .updateAt(chat.getUpdateAt())
                .users(chat.getUsers())
                .build();
    }
    public static RoomDto entityToDto(Room room){

        return RoomDto.builder()
                .roomName(room.getRoomName())
                .roomInfo(room.getRoomInfo())
                .updateAt(room.getUpdateAt())
                .createAt(room.getCreateAt())
                .deleteAt(room.getDeleteAt())
                .houseId(room.getHouseId())
                .houseImg(room.getHouseImg())
                .id(room.getId())
                .build();
    }
    public static RoomResponse entityToVo(Room room){

        return RoomResponse.builder()
                .roomName(room.getRoomName())
                .roomInfo(room.getRoomInfo())
                .updateAt(room.getUpdateAt())
                .createAt(room.getCreateAt())
                .deleteAt(room.getDeleteAt())
                .houseId(room.getHouseId())
                .houseImg(room.getHouseImg())
                .id(room.getId())
                .build();
    }
    public static Room dtoToEntity(RoomDto roomDto){
        return Room.builder()
                .roomName(roomDto.getRoomName())
                .roomInfo(roomDto.getRoomInfo())
                .updateAt(roomDto.getUpdateAt())
                .createAt(roomDto.getCreateAt())
                .deleteAt(roomDto.getDeleteAt())
                .houseId(roomDto.getHouseId())
                .houseImg(roomDto.getHouseImg())
                .id(roomDto.getId())
                .build();
    }
//    public static RoomInfoDto entityToDto(RoomInfo roomInfo){
//        return RoomInfoDto.builder()
//                .lastSentUserId(roomInfo.getLastSentUserId())
//                .lastMessage(roomInfo.getLastMessage())
//                .users(roomInfo.getUsers())
//                .build();
//    }
//    public static RoomInfo dtoToEntity(RoomInfoDto roomInfoDto){
//        return RoomInfo.builder()
//                .lastSentUserId(roomInfoDto.getLastSentUserId())
//                .lastMessage(roomInfoDto.getLastMessage())
//                .users(roomInfoDto.getUsers())
//                .build();
//    }
//    public static RoomInfo toEntity(RoomDto roomDto){
//        List<UserInfo> users = new ArrayList<>();
//        for (Long userId:roomDto.getRoomInfo().getUsers().) {
//            users.add(UserInfo.builder()
//                    .userId(userId)
//                    .readCount(0)
//                    .delete(false)
//                    .build());
//        }
//        return RoomInfo.builder()
//                .updateAt(null)
//                .lastSentUserId(null)
//                .lastMessage(null)
//                .createAt(null)
//                .deleteAt(null)
//                .users(users)
//                .build();
//    }
//    public static Mono<RoomInfoDto> toEntity(Mono<RoomDto> roomDto1){
//        List<UserInfo> users = new ArrayList<>();
//        return   roomDto1.map(roomDto -> {
//            for (Long userId:roomDto.getUserIds()) {
//                users.add(UserInfo.builder()
//                        .userId(userId)
//                        .readCount(0)
//                        .delete(false)
//                        .build());
//            }
//            return RoomInfoDto.builder()
//                    .updateAt(null)
//                    .lastSentUserId(null)
//                    .lastMessage(null)
//                    .createAt(null)
//                    .deleteAt(null)
//                    .users(users)
//                    .build();
//        });
//    }
    public static RoomDto voToDto(RoomRequest roomRequest){
        List<UserInfo> users = new ArrayList<>();
        for (Long userId:roomRequest.getUserIds()) {
            users.add(UserInfo.builder()
                    .userId(userId)
                    .readCount(0)
                    .connect(false)
                    .createAt(LocalDateTime.now())
                    .build());
        }
        RoomInfo roomInfo = RoomInfo.builder()
                .lastMessage(null)
                .lastSentUserId(null)
                .users(users)
                .lastSentUserName(null)
                .build();
        return RoomDto.builder()
                .houseId(roomRequest.getHouseId())
                .roomInfo(roomInfo)
                .houseImg(roomRequest.getHouseImg())
                .roomName(roomRequest.getRoomName())
                .build();
    }


}
