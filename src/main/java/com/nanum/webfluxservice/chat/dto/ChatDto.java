package com.nanum.webfluxservice.chat.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;


@Data
@Builder
public class ChatDto {
    private String id;
    private String msg;

    private String userId;

    private String updateAt;
    private String roomId;
    private LocalDateTime createAt;
    private boolean delete;
}
