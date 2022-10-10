package com.nanum.webfluxservice.chat.application;

import com.nanum.webfluxservice.chat.domain.RoomInfo;
import com.nanum.webfluxservice.chat.dto.RoomDto;
import com.nanum.webfluxservice.chat.dto.RoomInfoDto;
import reactor.core.publisher.Mono;

public interface RoomInfoService {

    Mono<RoomInfoDto> saveRoomInfo(Mono<RoomInfoDto> roomInfoDtoMono);
    Mono<RoomInfo> insertRoomInfo(RoomInfo roomInfo);
    Mono<RoomInfo> saveRoomDto(Mono<RoomDto> roomDtoMono);
}
