package com.nanum.webfluxservice.alert.application;

import com.nanum.webfluxservice.alert.domain.Alert;
import com.nanum.webfluxservice.alert.dto.AlertDto;
import com.nanum.webfluxservice.alert.infrastructure.AlertRepository;
import com.nanum.webfluxservice.alert.utils.AppUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.data.domain.Range;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class AlertServiceImpl implements AlertService{
    private final AlertRepository alertRepository;

    @Override
    public Flux<AlertDto> getAlerts() {
           return alertRepository.findAll().map(AppUtils::entityToDto);
    }

    @Override
    public Mono<AlertDto> getAlert(String id) {
        return alertRepository.findById(id).map(AppUtils::entityToDto);
    }

    @Override
    public Flux<AlertDto> getAlertInRange(double min, double max) {
        return alertRepository.findByPriceBetween(Range.closed(min, max));
    }

    @Override
    public Mono<AlertDto> saveAlert(Mono<AlertDto> alertDtoMono) {
        return alertDtoMono.map(AppUtils::dtoToEntity)
                .flatMap(alertRepository::insert)
                .map(AppUtils::entityToDto);
    }

    @Override
    public Mono<AlertDto> updateAlert(Mono<AlertDto> alertDtoMono, String id) {


        return alertRepository.findById(id)
                .flatMap(alert -> alertDtoMono.map(alertDto ->
                        {
                            alertDto.setId(id);
                            return AppUtils.dtoToEntity(alertDto);
                        }))
                .flatMap(alertRepository::save)
                .map(AppUtils::entityToDto);

//        return alertRepository.findById(id)
//                .map(alert -> AppUtils::entityToDto)
//                .map(alert -> modelMapper.map(alert, AlertDto.class))
//                .doOnNext(alertDto -> alertDto.setId(id))
//                .flatMap(alert -> alertDtoMono.map(AlertDto::DtoToEntity))
//                .flatMap(alertRepository::save)
//                .map(alert -> modelMapper.map(alert, AlertDto.class));
    }

    @Override
    public Mono<Void> deleteAlert(String id) {
        return alertRepository.deleteById(id);
    }


}
