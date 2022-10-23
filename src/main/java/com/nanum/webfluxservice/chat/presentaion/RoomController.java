package com.nanum.webfluxservice.chat.presentaion;

import com.nanum.config.BaseResponse;
import com.nanum.webfluxservice.alert.dto.AlertDto;
import com.nanum.webfluxservice.chat.application.RoomService;
import com.nanum.webfluxservice.chat.domain.Room;
import com.nanum.webfluxservice.chat.dto.ChatDto;
import com.nanum.webfluxservice.chat.dto.RoomDto;
import com.nanum.webfluxservice.chat.utils.AppUtils;

import com.nanum.webfluxservice.chat.utils.SSeServiceImpl;
import com.nanum.webfluxservice.chat.vo.RoomRequest;
import com.nanum.webfluxservice.chat.vo.RoomResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import javax.validation.Valid;
import java.time.Duration;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;


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
    private final SSeServiceImpl sse;
    @Operation(summary = "채팅방 생성 API", description = "채팅을 해당 관련된 id를 추가하여 채팅을 만듭니다.")
    @PostMapping
    public ResponseEntity<Mono<RoomDto>> saveRoom(@Valid @RequestBody Mono<RoomRequest> roomRequestMono){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(roomService.save(roomRequestMono.map(AppUtils::voToDto)));
//                .then(Mono.just(new ResponseEntity<RoomDto>(HttpStatus.OK)))
//                .(new ResponseEntity<>(HttpStatus.NOT_FOUND));

    }

    @Operation(summary = "채팅방 조회 API", description = "전체조회.")
    @GetMapping
    public ResponseEntity<Flux<RoomDto>> retrieveRooms(){
        return ResponseEntity.status(HttpStatus.OK).body(roomService.getRooms());
    }

    @Operation(summary = "채팅방 상세 조회 API", description = "전체조회.")
    @GetMapping("/{roomId}")
    public ResponseEntity<Mono<RoomDto>> retrieveRoom(@PathVariable("roomId") String roomId){
        return ResponseEntity.status(HttpStatus.OK).body(roomService.getRoom(roomId));
    }

    @Operation(summary = "유저별 채팅방 조회 API", description = "유저별 채팅방 전체조회.")
    @GetMapping("/users/{userId}")
    public ResponseEntity<Flux<RoomDto>> retrieveRoomsByUserId(@PathVariable("userId") Long userId){
        return ResponseEntity.status(HttpStatus.OK).body(roomService.getRoomsByUserId(userId));
    }

    @Operation(summary = "기존 채팅방 상세 조회 API", description = "기존 채팅방에 있을 경우 가져오고 아닐 경우 빈 값 조회")
    @GetMapping("/houses/{houseId}")
    public Mono<ResponseEntity<RoomResponse>> retrieveRoomByUserIdAndHouseId(@PathVariable("houseId") Long houseId,
                                                                             @RequestParam(value="users", required=false, defaultValue="")
                                                                            List<Long> params){
        return roomService.getRoomByUserIdAndHouseId(params, houseId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "유저별 채팅방 나가기 API", description = "해당 유저의 채팅방 나가기 조회.")
    @DeleteMapping("/{id}/users/{userId}")
    public ResponseEntity<Mono<RoomDto>> deleteRoomsByUserId(@PathVariable("id") String id,
                                            @PathVariable("userId") Long userId){
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(roomService.deleteRoomByUserId(id, userId));
//                .then(Mono.just(new ResponseEntity<RoomDto>(HttpStatus.NO_CONTENT)))
//                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "채팅방 추가 API", description = "해당 유저의 채팅방 초대.")
    @PutMapping("/{id}/users/{userId}")
    public Mono<ResponseEntity<RoomDto>> updateRoomsByUserId(@PathVariable("id") String id,
                                             @PathVariable("userId") Long userId){
        return roomService.updateRoomByUserId(id, userId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    @CrossOrigin
    @GetMapping(value = "/sse/{userId}")
    public Flux<ServerSentEvent<Room>> streamEvents(@PathVariable("userId") Long userId) {

      return  sse.wrap(roomService.findAllBySSE(userId));
//        return Flux.interval(Duration.ofSeconds(1))
//                .map(sequence -> ServerSentEvent.<String> builder()
//                        .id(String.valueOf(sequence))
//                        .event("ev")
//                        .data("SSE - " + LocalTime.now().toString())
//                        .build());
    }
    @Operation(summary = "기존에 있는 방인지 확인 API", description = "기존에 있는 방인지 확인합니다.")
    @CrossOrigin
    @GetMapping(value = "/users", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Mono<ResponseEntity<Boolean>> subscribeUser(@RequestParam(value="param", required=false, defaultValue="")
                                                       List<Long> params){

        return roomService.validChatRoom(params)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    @GetMapping("/users/{userId}/count")
    public Mono<ResponseEntity<HashMap<String, Integer>>> countAllRoomsByReadMark(@PathVariable("userId") Long userId){

        return roomService.countAllRoomsByReadMark(userId)
                .map(ResponseEntity::ok);
    }
}
