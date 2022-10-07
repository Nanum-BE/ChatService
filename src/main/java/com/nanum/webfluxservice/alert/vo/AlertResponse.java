package com.nanum.webfluxservice.alert.vo;

import com.nanum.webfluxservice.alert.domain.User;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class AlertResponse {
    private String id;
    private String content;
    private String title;
    private List<User> users;
    private String url;
    private LocalDateTime createAt;

}
