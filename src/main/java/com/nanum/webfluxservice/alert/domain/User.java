package com.nanum.webfluxservice.alert.domain;

import lombok.Data;

@Data
public class User {
    private Long userId;
    private boolean readMark;
    private boolean delete;
}
