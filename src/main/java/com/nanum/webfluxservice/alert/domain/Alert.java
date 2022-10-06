package com.nanum.webfluxservice.alert.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.List;

@Document
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Alert {

    @Id
    private String id;

    private String content;

    private List<Long> userIds;
    private List<Long> readByUserIds;


    @CreatedDate
    private LocalDateTime createAt;

    private List<Long> deletedByUserIds;
    private LocalDateTime deleteAt;

//    private String name;
//    private int qty;
//    private double price;

}