package com.nanum.webfluxservice.chat.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.nanum.webfluxservice.alert.domain.User;
import com.nanum.webfluxservice.chat.domain.Room;
import com.nanum.webfluxservice.chat.domain.UserInfo;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class RoomInfoDto {

//    private String id;
    private String lastMessage;
    private String lastSentUserId;

//    private Room room;
    private List<UserInfo> users;

//    private LocalDateTime createAt;
//
//    private LocalDateTime updateAt;
//
//    private LocalDateTime deleteAt;
}
