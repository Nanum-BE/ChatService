package com.nanum.webfluxservice.chat.domain;

import com.nanum.webfluxservice.alert.domain.User;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Data
@Builder
public class RoomInfo {

    private String lastMessage;

    private Long lastSentUserId;
    private String lastSentUserName;

    private String updateAt;
    private List<UserInfo> users;

//    @DocumentReference(lazy = false, lookup = "{ 'primaryRoomInfo': ?#{#self._id}")
//    @ReadOnlyProperty
//    private Room room;

//    @CreatedDate
//    private LocalDateTime createAt;
//
//    @LastModifiedDate
//    private LocalDateTime updateAt;
//
//    private LocalDateTime deleteAt;
}
