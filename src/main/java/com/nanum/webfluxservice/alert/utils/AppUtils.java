package com.nanum.webfluxservice.alert.utils;

import com.nanum.webfluxservice.alert.domain.Alert;
import com.nanum.webfluxservice.alert.dto.AlertDto;
import com.nanum.webfluxservice.alert.vo.AlertRequest;

public class AppUtils {
    public static Alert dtoToEntity(AlertDto alertDto){
        return Alert.builder()
                .id(alertDto.getId())
                .content(alertDto.getContent())
                .createAt(alertDto.getCreateAt())
                .userIds(alertDto.getUserIds())
                .deleteAt(alertDto.getDeleteAt())
                .deletedByUserIds(alertDto.getDeletedByUserIds())
                .readByUserIds(alertDto.getReadByUserIds())
//                .name(alertDto.getName())
//                .qty(alertDto.getQty())
//                .price(alertDto.getPrice())
                .build();
    }
    public static AlertDto entityToDto(Alert alert){
        return AlertDto.builder()
                .id(alert.getId())
                .content(alert.getContent())
                .createAt(alert.getCreateAt())
                .userIds(alert.getUserIds())
                .deletedByUserIds(alert.getDeletedByUserIds())
                .readByUserIds(alert.getReadByUserIds())
                .deleteAt(alert.getDeleteAt())
                .build();
    }
    public static AlertDto voToDto(AlertRequest alertRequest){
        return AlertDto.builder()
                .content(alertRequest.getContent())
                .userIds(alertRequest.getUserIds())
                .build();
    }
}
