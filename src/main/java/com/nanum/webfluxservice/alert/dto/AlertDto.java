package com.nanum.webfluxservice.alert.dto;
import com.nanum.webfluxservice.alert.domain.Alert;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
@Builder
public class AlertDto {

    private String id;
    private String name;
    private int qty;
    private double price;

}
