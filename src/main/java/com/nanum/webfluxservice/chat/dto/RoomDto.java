package com.nanum.webfluxservice.chat.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.nanum.webfluxservice.chat.domain.RoomInfo;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class RoomDto {
    private String id;
    private List<Long> userIds;
    private String roomName;
    private String roomInfoId;
    private Long houseId;
    private LocalDateTime createAt;

    private LocalDateTime updateAt;

    private LocalDateTime deleteAt;

}
