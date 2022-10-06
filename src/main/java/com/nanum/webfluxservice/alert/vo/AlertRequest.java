package com.nanum.webfluxservice.alert.vo;

import com.nanum.webfluxservice.alert.domain.User;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class AlertRequest {
    @NotNull(message = "content cannot be null")
    @Size(min = 1)
    private String content;

    @NotNull(message = "userIds cannot be null")
    @Size(min = 1)
    private List<User> users;

    @NotNull(message = "url cannot be null")
    @Size(min = 1)
    private String url;

}

