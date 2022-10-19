package com.nanum.webfluxservice.chat.domain;

import com.nanum.webfluxservice.alert.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Chat {

    @Id
    private String id;

    private String roomId;

    private String userId;

    private String username;

    private String type;
    private String msg;

    private String updateAt;
    @CreatedDate
    private LocalDateTime createAt;

    private boolean delete;
}
