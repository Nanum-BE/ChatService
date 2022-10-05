package com.nanum.webfluxservice.alert.presentation;

import com.nanum.webfluxservice.alert.application.AlertService;
import com.nanum.webfluxservice.alert.domain.Alert;
import com.nanum.webfluxservice.alert.dto.AlertDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

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

    @GetMapping("/alert-range")
    public Flux<AlertDto> getAlertBetweenRange(@RequestParam("min") double min, @RequestParam("max") double max){
        return alertService.getAlertInRange(min, max);
    }

    @PostMapping
    public Mono<AlertDto> saveAlert(@RequestBody Mono<AlertDto> alertDtoMono){
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

}
