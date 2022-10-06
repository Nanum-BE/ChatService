package com.nanum.webfluxservice.alert.dto;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.nanum.webfluxservice.alert.domain.Alert;
import com.nanum.webfluxservice.alert.domain.User;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class AlertDto {

    private String id;
    private String content;
//    private List<Long> userIds;
//    private List<Long> readByUserIds;


    private List<User> users;
    private LocalDateTime createAt;
    private boolean deleteAt;
//    private List<Long> deletedByUserIds;
    private String url;
    //    private String name;
//    private int qty;
//    private double price;
}
