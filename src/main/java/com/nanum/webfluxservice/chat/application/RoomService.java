package com.nanum.webfluxservice.chat.application;

import com.nanum.webfluxservice.chat.domain.Room;
import com.nanum.webfluxservice.chat.dto.RoomDto;
import com.nanum.webfluxservice.chat.vo.RoomResponse;
import io.swagger.v3.oas.models.media.Schema;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface RoomService {

    /*
    1. 채팅방 생성
    2. 채팅방 목록 가져오기
    3. 유저별 채팅방 가져오기
    4. 채팅방 삭제
    5. 채팅방 인원 추가, 또는 나가기
     */
    Mono<RoomDto> save(Mono<RoomDto> roomDtoMono);

    Mono<Void> updatedConnectRoomByIdByUserId(String id, Long userId);
    Mono<Void> cancelConnectRoomByIdByUserId(String id, Long userId);


    Flux<RoomDto> getRooms();

    Flux<RoomDto> getRoomsByUserId(Long userId);

    Mono<RoomDto> deleteRoomByUserId(String id,Long userId);

    Mono<RoomDto> updateRoomByUserId(String id, Long userId);


    Mono<RoomDto> updateRoomByUserIdV2(String id, Long userId,String username);

    Mono<Void> updateCountByRoomIdAndMsg(String id,String msg);

    Mono<Void> updateCountByRoomIdAndMsgAndSendSSE(String id,String msg);

    public Flux<Room> findAllBySSE(Long userId);

    Mono<Void> updatedConnectRoomByIdByUserIdV2(String roomId, Long userId, String username);


    Mono<Boolean> validChatRoom(List<Long> params);

    Mono<RoomDto> getRoom(String roomId);

    Mono<RoomResponse> getRoomByUserIdAndHouseId(List<Long> params, Long houseId);

    Mono<HashMap<String,Integer>> countAllRoomsByReadMark(Long userId);

    Mono<Void> updateCountByRoomIdAndMsgAndSendSSEV2(String roomId, String msg, Map<String, Object> fromJson);
}
