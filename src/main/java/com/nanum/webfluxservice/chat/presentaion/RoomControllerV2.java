package com.nanum.webfluxservice.chat.presentaion;

import com.nanum.webfluxservice.chat.application.RoomService;
import com.nanum.webfluxservice.chat.dto.RoomDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/rooms")
@Tag(name = "채팅방", description = "채팅방 관련 api")
@ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "created successfully",
                content = @Content(schema = @Schema(defaultValue = " 등록 신청이 완료되었습니다."))),
        @ApiResponse(responseCode = "400", description = "bad request",
                content = @Content(schema = @Schema(defaultValue = "잘못된 입력 값입니다."))),
        @ApiResponse(responseCode = "500", description = "server error",
                content = @Content(schema = @Schema(defaultValue = "서버 에러입니다."))),
})
public class RoomControllerV2 {
    private final RoomService roomService;


    @Operation(summary = "채팅방 추가 API", description = "해당 유저의 채팅방 초대.")
    @PutMapping("/{id}/users/{userId}/name/{username}")
    public Mono<ResponseEntity<RoomDto>> updateRoomsByUserId(@PathVariable("id") String id,
                                                             @PathVariable("userId") Long userId,
                                                             @PathVariable("username") String userName){
        return roomService.updateRoomByUserIdV2(id, userId,userName)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
