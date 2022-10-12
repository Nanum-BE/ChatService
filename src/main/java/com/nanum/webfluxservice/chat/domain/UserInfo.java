package com.nanum.webfluxservice.chat.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserInfo {
    private Long userId;
    private int readCount;
    private boolean connect;
}
