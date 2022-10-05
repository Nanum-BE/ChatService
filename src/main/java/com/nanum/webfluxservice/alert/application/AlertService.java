package com.nanum.webfluxservice.alert.application;

import com.nanum.webfluxservice.alert.dto.AlertDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AlertService {

    Flux<AlertDto> getAlerts();

    Mono<AlertDto> getAlert(String id);

    Flux<AlertDto> getAlertInRange(double min, double max);

    Mono<AlertDto> saveAlert(Mono<AlertDto> alertDtoMono);

    Mono<AlertDto> updateAlert(Mono<AlertDto> alertDtoMono, String id);

    Mono<Void> deleteAlert(String id);
}
