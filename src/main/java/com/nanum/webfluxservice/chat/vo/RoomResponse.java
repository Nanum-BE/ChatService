package com.nanum.webfluxservice.chat.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.nanum.webfluxservice.chat.domain.RoomInfo;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoomResponse {
    private String id;
    //    private List<Long> userIds;
    private String roomName;
    private RoomInfo roomInfo;
    private String houseImg;
    private Long houseId;
    private LocalDateTime createAt;

    private LocalDateTime updateAt;

    private LocalDateTime deleteAt;

}
