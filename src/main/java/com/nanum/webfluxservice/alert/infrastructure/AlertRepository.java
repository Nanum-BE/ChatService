package com.nanum.webfluxservice.alert.infrastructure;

import com.nanum.webfluxservice.alert.domain.Alert;
import com.nanum.webfluxservice.alert.dto.AlertDto;
import org.springframework.data.domain.Range;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface AlertRepository extends ReactiveMongoRepository<Alert, String> {
    Flux<AlertDto> findByPriceBetween(Range<Double> priceRange);
}
