package com.nanum.webfluxservice.alert.presentation;

import com.nanum.webfluxservice.alert.application.AlertService;
import com.nanum.webfluxservice.alert.domain.Alert;
import com.nanum.webfluxservice.alert.dto.AlertDto;
import com.nanum.webfluxservice.alert.utils.AppUtils;
import com.nanum.webfluxservice.alert.vo.AlertRequest;
import com.nanum.webfluxservice.alert.vo.AlertResponse;
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
import reactor.core.scheduler.Schedulers;

import javax.validation.Valid;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/alerts")
@Tag(name = "알림", description = "알림 관련 api")
@ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "created successfully",
                content = @Content(schema = @Schema(defaultValue = " 등록 신청이 완료되었습니다."))),
        @ApiResponse(responseCode = "400", description = "bad request",
                content = @Content(schema = @Schema(defaultValue = "잘못된 입력 값입니다."))),
        @ApiResponse(responseCode = "500", description = "server error",
                content = @Content(schema = @Schema(defaultValue = "서버 에러입니다."))),
})
public class AlertController {

    private final AlertService alertService;
    @Operation(summary = "알림 전체 조회 API", description = "알림을 모두 출력합니다.")
    @GetMapping
    public ResponseEntity<Flux<AlertDto>> retrieveAlerts(){
        return ResponseEntity.status(HttpStatus.OK).body(alertService.getAlerts());
    }

    @Operation(summary = "알림 상세 조회 API", description = "알림을 하나만 출력합니다.")
    @GetMapping("/{id}")
    public Mono<ResponseEntity<AlertDto>> retrieveAlert(@PathVariable String id){
        return alertService.getAlert(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(summary = "알림 생성 API", description = "알림을 해당 관련된 id를 추가하여 알림을 만듭니다.")
    @PostMapping
    public Mono<ResponseEntity<AlertDto>> saveAlert(@Valid @RequestBody Mono<AlertRequest> alertRequestMono){
        Mono<AlertDto> alertDtoMono = alertRequestMono.map(AppUtils::voToDto);
        return alertService.saveAlert(alertDtoMono)
                .then(Mono.just(new ResponseEntity<AlertDto>(HttpStatus.CREATED)))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    @Operation(summary = "알림 수정 API", description = "알림을 수정합니다.(테스트중)")
    @PutMapping("/{id}")
    public Mono<ResponseEntity<AlertDto>> updateAlert(@RequestBody Mono<AlertDto> alertDtoMono, @PathVariable String id){
        return  alertService.updateAlert(alertDtoMono,id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    @Operation(summary = "알림 삭제 API", description = "해당 알림을 삭제합니다.")
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteAlert(@PathVariable String id){
        return alertService.deleteAlert(id)
                .then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    @Operation(summary = "알림 기능 받기 API", description = "알림기능을 이용하기 위해 유저와 연결합니다.")
    @CrossOrigin
    @GetMapping(value = "/users", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Mono<ResponseEntity<AlertDto>> subscribeUser(@RequestParam(value="param", required=false, defaultValue="")
                                            List<Long> params){
        return alertService.connect(params).subscribeOn(Schedulers.boundedElastic())
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    @Operation(summary = "유저별 알림 전체 조회 API", description = "해당 유저가 볼수 있는 알림을 모두 출력합니다.")
    @GetMapping("/users/{id}")
    public ResponseEntity<Flux<AlertResponse>>  retrieveAlertByUsers(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK)
                .body(alertService.getAlertsByUser(id).map(AppUtils::toVo));
    }
    @Operation(summary = "유저별 알림 개수 조회 API", description = "해당 유저가 못본 알림 개수를 출력합니다.")
    @GetMapping("/count/users/{userId}")
    public Mono<ResponseEntity<Long>> getAlertsByUserByCount(@PathVariable("userId") Long userId) {

        return alertService.getAlertsByUserByCount(userId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());

    }
    @Operation(summary = "유저별 알림 삭제 API", description = "유저별 원하지 않는 알림을 모두 식제합니다.")
    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Flux<Alert>> deleteAlertsByIdFromUser(@RequestParam(value="alertIds", required=false, defaultValue="")
                                                                   List<String> alertIds
            , @PathVariable("userId") Long userId) {

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(alertService.deleteAlertsByIdsFromUser(alertIds, userId));
    }

//    @GetMapping("/stream-sse")
//    public Flux<ServerSentEvent<String>> streamEvents() {
//        return Flux.interval(Duration.ofSeconds(1))
//                .map(sequence -> ServerSentEvent.<String> builder()
//                        .id(String.valueOf(sequence))
//                        .event("periodic-event")
//                        .data("SSE - " + LocalTime.now().toString())
//                        .build());
//    }
//    @GetMapping(value = "/fluxstream", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
//    public Flux<Integer> fluxstream(){
//        return Flux.just(1,2,3,4,5).delayElements(Duration.ofSeconds(1)).log();
//    }


//    @CrossOrigin
//    @GetMapping(value = "/subscribe/{id}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
//    public Flux<AlertDto> getSubscribe(@PathVariable("id") Long id,
//                                       @RequestHeader(value = "Last-Event-ID"
//                                               , required = false
//                                                ,defaultValue = "") String lastEventId){
//        return alertService.subscribe(id, lastEventId);
//    }
}
