package com.nanum.webfluxservice.alert.dto;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.nanum.webfluxservice.alert.domain.Alert;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AlertDto {

    private String id;
    private String content;
    private List<Long> userIds;
    private List<Long> readByUserIds;
    private LocalDateTime createAt;
    private LocalDateTime deleteAt;
    private List<Long> deletedByUserIds;

    //    private String name;
//    private int qty;
//    private double price;
}
