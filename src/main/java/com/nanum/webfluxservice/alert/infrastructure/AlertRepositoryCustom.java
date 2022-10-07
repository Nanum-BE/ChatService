package com.nanum.webfluxservice.alert.infrastructure;

import com.nanum.webfluxservice.alert.domain.Alert;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
@Repository
public interface AlertRepositoryCustom {

    Flux<Alert> findAllReadByUsers(Long userId);
}
