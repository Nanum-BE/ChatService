package com.nanum.webfluxservice.alert.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class AlertRequest {
    @NotNull(message = "title cannot be null")
    @Size(min = 1)
    private String content;

    @NotNull(message = "content cannot be null")
    @Size(min = 1)
    private List<Long> userIds;

}

