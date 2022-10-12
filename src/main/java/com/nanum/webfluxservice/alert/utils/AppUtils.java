package com.nanum.webfluxservice.alert.utils;

import com.nanum.webfluxservice.alert.domain.Alert;
import com.nanum.webfluxservice.alert.domain.User;
import com.nanum.webfluxservice.alert.dto.AlertDto;
import com.nanum.webfluxservice.alert.vo.AlertRequest;
import com.nanum.webfluxservice.alert.vo.AlertResponse;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AppUtils {
    public static Alert dtoToEntity(AlertDto alertDto){
        return Alert.builder()
                .id(alertDto.getId())
                .content(alertDto.getContent())
                .createAt(alertDto.getCreateAt())
                .users(alertDto.getUsers())
                .deleteAt(alertDto.isDeleteAt())
                .url(alertDto.getUrl())
                .title(alertDto.getTitle())
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
                .users(alert.getUsers())
                .deleteAt(alert.isDeleteAt())
                .url(alert.getUrl())
                .title(alert.getTitle())
                .build();
    }
    public static AlertDto voToDto(AlertRequest alertRequest){
        List<User> users = new ArrayList<>();
        for (Long userIds: alertRequest.getUserIds()) {
            users.add(User.builder()
                    .delete(false)
                    .readMark(false)
                    .userId(userIds)
                    .build());
        }
        return AlertDto.builder()
                .content(alertRequest.getContent())
                .users(users)
                .title(alertRequest.getTitle())
                .url(alertRequest.getUrl())
                .createAt(null)
                .deleteAt(false)
                .build();
    }

    public static AlertResponse toVo(AlertDto alertDto){
        return AlertResponse.builder()
                .content(alertDto.getContent())
                .url(alertDto.getUrl())
                .createAt(alertDto.getCreateAt())
                .id(alertDto.getId())
                .users(alertDto.getUsers())
                .build();
    }
}
