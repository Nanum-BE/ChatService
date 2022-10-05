package com.nanum.webfluxservice.alert.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Alert {

    @Id
    private String id;
    private String name;
    private int qty;
    private double price;

}