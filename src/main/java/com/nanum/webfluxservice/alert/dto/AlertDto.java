package com.nanum.webfluxservice.alert.dto;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.nanum.webfluxservice.alert.domain.Alert;
import com.nanum.webfluxservice.alert.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class AlertDto {
    @Schema(description = "해당 알림 ID",defaultValue = "0")
    private String id;
    @Schema(description = "해당 알림 내용",defaultValue = "0")
    private String content;
//    private List<Long> userIds;
//    private List<Long> readByUserIds;
    private String title;

    private List<User> users;
    private LocalDateTime createAt;
    private boolean deleteAt;
//    private List<Long> deletedByUserIds;
    private String url;
    //    private String name;
//    private int qty;
//    private double price;
}
