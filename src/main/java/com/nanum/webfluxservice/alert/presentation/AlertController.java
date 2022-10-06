package com.nanum.webfluxservice.alert.presentation;

import com.nanum.webfluxservice.alert.application.AlertService;
import com.nanum.webfluxservice.alert.dto.AlertDto;
import com.nanum.webfluxservice.alert.utils.AppUtils;
import com.nanum.webfluxservice.alert.vo.AlertRequest;
import com.nanum.webfluxservice.chat.dto.ChatDto;
import com.nanum.webfluxservice.config.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
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
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/alerts")
public class AlertController {

    private final AlertService alertService;
    @GetMapping
    public Flux<AlertDto> retrieveAlerts(){
        return alertService.getAlerts();
    }

    @GetMapping("/{id}")
    public Mono<AlertDto> retrieveAlert(@PathVariable String id){
        return alertService.getAlert(id);
    }

//    @GetMapping("/alert-range")
//    public Flux<AlertDto> getAlertBetweenRange(@RequestParam("min") double min, @RequestParam("max") double max){
//        return alertService.getAlertInRange(min, max);
//    }

    @PostMapping
    public Mono<AlertDto> saveAlert(@Valid @RequestBody Mono<AlertRequest> alertRequestMono){
        Mono<AlertDto> alertDtoMono = alertRequestMono.map(AppUtils::voToDto);
        return alertService.saveAlert(alertDtoMono);
    }

    @PutMapping("/{id}")
    public Mono<AlertDto> updateAlert(@RequestBody Mono<AlertDto> alertDtoMono, @PathVariable String id){
        return  alertService.updateAlert(alertDtoMono,id);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteAlert(@PathVariable String id){
        return alertService.deleteAlert(id);
    }

    @GetMapping(value = "/fluxstream", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<Integer> fluxstream(){
        return Flux.just(1,2,3,4,5).delayElements(Duration.ofSeconds(1)).log();
    }


    @CrossOrigin
    @GetMapping(value = "/subscribe/{id}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<AlertDto> getSubscribe(@PathVariable("id") Long id,
                                       @RequestHeader(value = "Last-Event-ID"
                                               , required = false
                                                ,defaultValue = "") String lastEventId){
        return alertService.subscribe(id, lastEventId);
    }
    @CrossOrigin
    @GetMapping(value = "/user", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<AlertDto> subscribeUser(@RequestParam(value="param", required=false, defaultValue="")
                                            List<Long> params){
        return alertService.connect(params).subscribeOn(Schedulers.boundedElastic());
    }

    @GetMapping("/user/{id}")
    public Flux<AlertDto> retrieveAlertByUsers(@PathVariable Long id){
        return alertService.getAlertsByUser(id);
    }
    @GetMapping("/stream-sse")
    public Flux<ServerSentEvent<String>> streamEvents() {
        return Flux.interval(Duration.ofSeconds(1))
                .map(sequence -> ServerSentEvent.<String> builder()
                        .id(String.valueOf(sequence))
                        .event("periodic-event")
                        .data("SSE - " + LocalTime.now().toString())
                        .build());
    }


}
