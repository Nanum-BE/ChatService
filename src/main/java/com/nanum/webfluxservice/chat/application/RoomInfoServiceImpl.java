//package com.nanum.webfluxservice.chat.application;
//
//import com.nanum.webfluxservice.chat.domain.Room;
//import com.nanum.webfluxservice.chat.domain.RoomInfo;
//import com.nanum.webfluxservice.chat.domain.UserInfo;
//import com.nanum.webfluxservice.chat.dto.RoomDto;
//import com.nanum.webfluxservice.chat.dto.RoomInfoDto;
//import com.nanum.webfluxservice.chat.infrastructure.RoomInfoRepository;
//import com.nanum.webfluxservice.chat.infrastructure.RoomRepository;
//import com.nanum.webfluxservice.chat.utils.AppUtils;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//import reactor.core.Disposable;
//import reactor.core.publisher.Mono;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Service
//@Slf4j
//@RequiredArgsConstructor
//public class RoomInfoServiceImpl implements RoomInfoService{
//    private final RoomRepository roomRepository;
//    private final RoomInfoRepository roomInfoRepository;
//    @Override
//    public Mono<RoomInfo> saveRoomDto(Mono<RoomDto> roomDtoMono) {
////        return roomDtoMono.map(AppUtils::toEntity)
////                .flatMap(roomInfoRepository::insert);
//        return null;
//    }
//    @Override
//    public Mono<RoomInfoDto> saveRoomInfo(Mono<RoomInfoDto> roomInfoDto) {
////            return roomInfoDto.map(AppUtils::dtoToEntity)
////                    .flatMap(roomInfoRepository::insert)
////                    .map(AppUtils::entityToDto);
//            return null;
////        Disposable disposable = roomRepository.findById(roomId)
//////                .switchIfEmpty(Mono.error(new RoomNotFoundException(String.format("ID[%s] not found",roomId))))
////                .map(AppUtils::entityToDto)
////                .map(roomDto -> {
////                    log.info("getRoomInfo", roomDto.getRoomInfo());
////                    log.info("roomDto", roomDto.getRoomName());
////
////                    if (roomDto.getRoomInfo() == null) {
////                        List<UserInfo> userInfos = new ArrayList<>();
////                        for (Long userId : roomDto.getUserIds()) {
////                            userInfos.add(UserInfo.builder()
////                                    .userId(userId)
////                                    .readCount(0)
////                                    .delete(false)
////                                    .build());
////                        }
////                        Mono<RoomInfo> roomInfoMono = roomInfoRepository.save(RoomInfo.builder()
////                                        .users(userInfos)
////                                        .createAt(null)
////                                        .deleteAt(null)
////                                        .updateAt(null)
////                                        .lastMessage(null)
////                                        .lastSentUserId(null)
////                                        .build())
////                                .map(roomInfo -> {
////                                    roomDto.setRoomInfo(roomInfo);
////                                    return roomInfo;
////                                });
////                    }
////                    return roomDto;
////                }).map(AppUtils::dtoToEntity)
////                .map(room -> {
////                    if (room.getRoomInfo().getCreateAt() == null) {
////                        roomRepository.save(room);
////                    }
////                    return AppUtils.entityToDto(room);
////                }).subscribe();
////        log.info("Disposable:",disposable);
//////        return null;
////             .map(AppUtils::entityToDto)
////                .map(room -> {
////                    log.info("roomId: " + roomId);
////                    log.info("room: " + room.getRoomName());
////                    if (room.getRoomInfo() == null) {
////                        List<UserInfo> userInfos = new ArrayList<>();
////                        for (Long userId : room.getUserIds()) {
////                            userInfos.add(UserInfo.builder()
////                                    .userId(userId)
////                                    .readCount(0)
////                                    .delete(false)
////                                    .build());
////                        }
////                        roomInfoRepository.save(RoomInfo.builder()
////                                        .users(userInfos)
////                                        .room(AppUtils.dtoToEntity(room))
////                                        .createAt(null)
////                                        .deleteAt(null)
////                                        .updateAt(null)
////                                        .lastMessage(null)
////                                        .lastSentUserId(null)
////                                        .build())
////                                .map(roomInfo -> {
////                                    room.setRoomInfo(roomInfo);
////                                    return roomInfo;
////                                }).subscribe();
////                        roomRepository.save(AppUtils.dtoToEntity(room))
////                                .subscribe();
////                    }
////                    return room;
////                }).subscribe();
//    }
//    @Override
//    public Mono<RoomInfo> insertRoomInfo(RoomInfo roomInfo) {
//        return roomInfoRepository.save(roomInfo);
//    }
//}
