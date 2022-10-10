package com.nanum.webfluxservice.chat.application;

import com.nanum.webfluxservice.chat.domain.RoomInfo;
import com.nanum.webfluxservice.chat.domain.UserInfo;
import com.nanum.webfluxservice.chat.dto.RoomDto;
import com.nanum.webfluxservice.chat.dto.RoomInfoDto;
import com.nanum.webfluxservice.chat.infrastructure.RoomInfoRepository;
import com.nanum.webfluxservice.chat.infrastructure.RoomRepository;
import com.nanum.webfluxservice.chat.utils.AppUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService{

    private final RoomRepository roomRepository;
    private final RoomInfoService roomInfoService;
    private final RoomInfoRepository roomInfoRepository;
    @Override
    public Mono<RoomDto> save(Mono<RoomDto> roomDtoMono) {
        return roomDtoMono.flatMap(roomDto -> {
            RoomInfo roomInfo = AppUtils.toEntity(roomDto);
            Mono<RoomDto> dtoMono = roomInfoRepository.insert(roomInfo)
                    .map(roomInfo1 -> {
                        roomDto.setRoomInfoId(roomInfo1.getId());
                        return roomDto;
                    });
        return dtoMono;
        }).map(AppUtils::dtoToEntity)
                .flatMap(roomRepository::insert)
                .map(AppUtils::entityToDto);
    }

    @Override
    public Flux<RoomDto> getRooms() {
        return roomRepository.findAll().map(AppUtils::entityToDto);
    }

    @Override
    public Flux<RoomDto> getRoomsByUserId(Long userId) {
        return roomRepository.findAllByUserIdsIn(userId)
                .map(AppUtils::entityToDto);
    }

    @Override
    public Mono<RoomDto> deleteRoomByUserId(String id, Long userId) {
        return roomRepository.findById(id)
                .map(AppUtils::entityToDto)
                .map(roomDto -> {

                    if(!roomDto.getUserIds().remove(userId)){
                        log.error(userId+"not found");
                    }
                    return roomDto;
                }).map(AppUtils::dtoToEntity)
                .flatMap(roomRepository::save)
                .map(AppUtils::entityToDto);
    }

    @Override
    public Mono<RoomDto> updateRoomByUserId(String id, Long userId) {
        return roomRepository.findById(id)
                .map(AppUtils::entityToDto)
                .map(roomDto -> {
                    if(!roomDto.getUserIds().contains(userId)){
                        roomDto.getUserIds().add(userId);
                    }else{
                        log.error(userId+"'s already existed");
                    }
                    return roomDto;
                }).map(AppUtils::dtoToEntity)
                .flatMap(roomRepository::save)
                .map(AppUtils::entityToDto);
    }
}
