package com.nanum.webfluxservice.chat.domain;

import com.nanum.webfluxservice.alert.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.time.LocalDateTime;
import java.util.List;

@Document
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class RoomInfo {

    @Id
    private String id;

    private String lastMessage;

    private String lastSentUserId;

    private List<UserInfo> users;

//    @DocumentReference(lazy = false, lookup = "{ 'primaryRoomInfo': ?#{#self._id}")
//    @ReadOnlyProperty
//    private Room room;

    @CreatedDate
    private LocalDateTime createAt;

    @LastModifiedDate
    private LocalDateTime updateAt;

    private LocalDateTime deleteAt;
}
