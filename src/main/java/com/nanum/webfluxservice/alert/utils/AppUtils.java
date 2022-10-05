package com.nanum.webfluxservice.alert.utils;

import com.nanum.webfluxservice.alert.domain.Alert;
import com.nanum.webfluxservice.alert.dto.AlertDto;

public class AppUtils {
    public static Alert dtoToEntity(AlertDto alertDto){
        return Alert.builder()
                .id(alertDto.getId())
                .name(alertDto.getName())
                .qty(alertDto.getQty())
                .price(alertDto.getPrice())
                .build();
    }
    public static AlertDto entityToDto(Alert alert){
        return AlertDto.builder()
                .id(alert.getId())
                .name(alert.getName())
                .qty(alert.getQty())
                .price(alert.getPrice())
                .build();
    }
}
