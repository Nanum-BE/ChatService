package com.nanum.webfluxservice.chat.presentaion;

import com.nanum.webfluxservice.chat.application.ChatService;
import com.nanum.webfluxservice.chat.dto.ChatDto;
import com.nanum.webfluxservice.chat.dto.RoomDto;
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
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chats")
@Tag(name = "채팅", description = "채팅 관련 api")
@ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "created successfully",
                content = @Content(schema = @Schema(defaultValue = " 등록 신청이 완료되었습니다."))),
        @ApiResponse(responseCode = "400", description = "bad request",
                content = @Content(schema = @Schema(defaultValue = "잘못된 입력 값입니다."))),
        @ApiResponse(responseCode = "500", description = "server error",
                content = @Content(schema = @Schema(defaultValue = "서버 에러입니다."))),
})
public class ChatController {
    private final ChatService chatService;

//    @CrossOrigin
//    @GetMapping(value = "/sender/{sender}/receiver/{receiver}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
//    public Flux<ChatDto> getMsg(@PathVariable("sender") Long sender, @PathVariable("receiver") Long receiver){
//        return chatService.findBySenderAndReceiver(sender,receiver).subscribeOn(Schedulers.boundedElastic());
//    }
//
//    @CrossOrigin
//    @GetMapping(value = "/room/{roomId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
//    public Flux<ChatDto> chat(@PathVariable("roomId") Long roomId){
//        return chatService.connectedRoom(roomId).subscribeOn(Schedulers.boundedElastic());
//    }
@Operation(summary = "유저별 채팅방 조회 API", description = "유저별 채팅방 전체조회.")
@GetMapping("rooms/{roomId}/users/{userId}")
public ResponseEntity<Flux<ChatDto>> retrieveChatsByUserId(@PathVariable("roomId") String roomId,
                                                           @PathVariable("userId") Long userId){
    return ResponseEntity.status(HttpStatus.OK).body(chatService.getChatsByRoomIdAndUserId(roomId,userId));
}


    @CrossOrigin
    @PostMapping
    public Mono<ChatDto> setMsg(@RequestBody ChatDto chatDto){
        chatDto.setCreateAt(LocalDateTime.now());

        return chatService.chatSave(chatDto);
    }

}
