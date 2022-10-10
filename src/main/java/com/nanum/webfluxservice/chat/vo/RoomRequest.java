package com.nanum.webfluxservice.chat.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;


@Data
public class RoomRequest {

    @NotNull(message = "userIds cannot be null")
    @Schema(description = "채팅방을 생성하기 위한 userId들을 입력하세요.",defaultValue = "[1, 2]")
    private List<Long> userIds;

    @NotNull(message = "roomName cannot be null")
    @Schema(description = "채팅방을 생성하기 위한 채팅방 이름을 입력하세요.기본값 이름들로 입력해주세요",defaultValue = "전호정, 강민수")
    private String roomName;

    @NotNull(message = "houseId cannot be null")
    @Schema(description = "채팅방을 생성하기 위한 해당 하우스의 ID값을 입력하세요.",defaultValue = "1")
    private Long houseId;

}
