package com.nanum.webfluxservice.chat.presentaion;

import com.nanum.webfluxservice.alert.dto.AlertDto;
import com.nanum.webfluxservice.chat.application.RoomService;
import com.nanum.webfluxservice.chat.dto.ChatDto;
import com.nanum.webfluxservice.chat.dto.RoomDto;
import com.nanum.webfluxservice.chat.utils.AppUtils;
import com.nanum.webfluxservice.chat.vo.RoomRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/rooms")
@Tag(name = "채팅방", description = "채팅방 관련 api")
@ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "created successfully",
                content = @Content(schema = @Schema(defaultValue = " 등록 신청이 완료되었습니다."))),
        @ApiResponse(responseCode = "400", description = "bad request",
                content = @Content(schema = @Schema(defaultValue = "잘못된 입력 값입니다."))),
        @ApiResponse(responseCode = "500", description = "server error",
                content = @Content(schema = @Schema(defaultValue = "서버 에러입니다."))),
})
public class RoomController {

    private final RoomService roomService;

    @Operation(summary = "채팅방 생성 API", description = "채팅을 해당 관련된 id를 추가하여 채팅을 만듭니다.")
    @PostMapping
    public Mono<ResponseEntity<RoomDto>> saveRoom(@Valid @RequestBody Mono<RoomRequest> roomRequestMono){
        return roomService.save(roomRequestMono.map(AppUtils::voToDto))
                .then(Mono.just(new ResponseEntity<RoomDto>(HttpStatus.CREATED)))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));

    }

    @Operation(summary = "채팅방 조회 API", description = "전체조회.")
    @GetMapping
    public ResponseEntity<Flux<RoomDto>> retrieveRooms(){
        return ResponseEntity.status(HttpStatus.OK).body(roomService.getRooms());
    }

    @Operation(summary = "유저별 채팅방 조회 API", description = "유저별 채팅방 전체조회.")
    @GetMapping("/users/{userId}")
    public ResponseEntity<Flux<RoomDto>> retrieveRoomsByUserId(@PathVariable("userId") Long userId){
        return ResponseEntity.status(HttpStatus.OK).body(roomService.getRoomsByUserId(userId));
    }

    @Operation(summary = "유저별 채팅방 나가기 API", description = "해당 유저의 채팅방 나가기 조회.")
    @DeleteMapping("/{id}/users/{userId}")
    public Mono<ResponseEntity<RoomDto>> deleteRoomsByUserId(@PathVariable("id") String id,
                                            @PathVariable("userId") Long userId){
        return roomService.deleteRoomByUserId(id, userId)
                .then(Mono.just(new ResponseEntity<RoomDto>(HttpStatus.NO_CONTENT)))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "채팅방 추가 API", description = "해당 유저의 채팅방 초대.")
    @PutMapping("/{id}/users/{userId}")
    public Mono<ResponseEntity<RoomDto>> updateRoomsByUserId(@PathVariable("id") String id,
                                             @PathVariable("userId") Long userId){
        return roomService.updateRoomByUserId(id, userId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

}
