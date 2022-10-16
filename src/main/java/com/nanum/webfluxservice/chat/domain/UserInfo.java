package com.nanum.webfluxservice.chat.domain;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Data
@Builder
public class UserInfo {
    private Long userId;
    private int readCount;
    private boolean connect;

    @CreatedDate
    private LocalDateTime createAt;

}
