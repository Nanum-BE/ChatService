package com.nanum.webfluxservice.alert.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User {
    private Long userId;
    private boolean readMark;
    private boolean delete;
}
