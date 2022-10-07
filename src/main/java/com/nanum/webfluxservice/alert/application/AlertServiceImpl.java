package com.nanum.webfluxservice.alert.application;
import com.nanum.webfluxservice.alert.domain.Alert;
import com.nanum.webfluxservice.alert.domain.User;
import com.nanum.webfluxservice.alert.dto.AlertDto;
import com.nanum.webfluxservice.alert.infrastructure.AlertRepository;
import com.nanum.webfluxservice.alert.utils.AppUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    public Flux<AlertDto> getAlertsByUser(Long userId) {

        Flux<Alert> alertFlux = alertRepository.findAllReadByUsers(userId)
                .map(AppUtils::entityToDto)
                .map(alertDto -> {
                    for (User user : alertDto.getUsers()) {
                        if (user.getUserId() == userId) {
                            user.setReadMark(true);
                        }
                    }
                    return alertDto;
                })
                .map(AppUtils::dtoToEntity);

        return alertRepository.saveAll(alertFlux)
                .map(AppUtils::entityToDto);

    }

    @Override
    public Mono<Long> getAlertsByUserByCount(Long userId) {
//        List<Long> users = new ArrayList<>();
//        users.add(userId);
//
//        return  alertRepository.countByUserIdsInAndDeletedByUserIdsNotInAndReadByUserIdsNotIn(users,users,users);
        return null;
    }

    @Override
    public Flux<AlertDto> getAlertInRange(double min, double max) {
//        return alertRepository.findByPriceBetween(Range.closed(min, max));
        return null;
    }

    @Override
    public Mono<AlertDto> saveAlert(Mono<AlertDto> alertDtoMono) {
        List<Long> initUser = new ArrayList<>();
        initUser.add(0L);
        return alertDtoMono.map(alertDto -> {
                    alertDto.setCreateAt(LocalDateTime.now());

                    return AppUtils.dtoToEntity(alertDto);
                })
                .flatMap(alertRepository::insert)
                .map(AppUtils::entityToDto);
    }

    @Override
    public Mono<AlertDto> updateAlert(Mono<AlertDto> alertDtoMono, String id) {

//        return alertRepository.findById(id)
//                .flatMap(alert -> alertDtoMono.map(alertDto ->
//                        {
//                            alertDto.setId(id);
//                            alertDto.setCreateAt(alert.getCreateAt());
//
//                            return AppUtils.dtoToEntity(alertDto);
//                        }))
//                .flatMap(alertRepository::save)
//                .map(AppUtils::entityToDto);
        return alertRepository.findById(id)
                .map(alert -> {
                    for (User user: alert.getUsers()) {
                        if(user.getUserId()==1){
                            user.setReadMark(true);
                        }
                    }
                    return alert;
                })
                .flatMap(alertRepository::save)
                .map(AppUtils::entityToDto);
    }

    @Override
    public Mono<Void> deleteAlert(String id) {
        return alertRepository.deleteAll();
    }

    @Override
    public Flux<AlertDto> subscribe(Long id, String lastEventId) {
        return null;
    }

    @Override
    public Mono<AlertDto> connect(List<Long> userId) {
        List<User> userEntity = new ArrayList<>();
        for (Long user: userId) {
            userEntity.add(User.builder().userId(user).build());
        }
        return alertRepository.findByUsersContainsAndCreateAtAfter(userEntity,LocalDateTime.now())
                .map(AppUtils::entityToDto);
    }

}
