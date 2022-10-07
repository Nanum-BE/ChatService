package com.nanum.webfluxservice.alert.application;

import com.nanum.webfluxservice.alert.dto.AlertDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface AlertService {

    /*
        1. 누구한테 보낼지 선택 생성
        2. 자기거 가져오기 목록
        3. 읽지 않은 거 카운트 가져오기
        3. 알림 가져오기
        4. 삭제
        자기거 가져오기
        알람 삭제
     */
    Flux<AlertDto> getAlerts();

    Mono<AlertDto> getAlert(String id);

    Flux<AlertDto> getAlertsByUser(Long userId);

    Mono<Long> getAlertsByUserByCount(Long userId);
    Flux<AlertDto> getAlertInRange(double min, double max);

    Mono<AlertDto> saveAlert(Mono<AlertDto> alertDtoMono);

    Mono<AlertDto> updateAlert(Mono<AlertDto> alertDtoMono, String id);

    Mono<Void> deleteAlert(String id);


    Flux<AlertDto> subscribe(Long id, String lastEventId);

    Mono<AlertDto> connect(List<Long> userId);
}
