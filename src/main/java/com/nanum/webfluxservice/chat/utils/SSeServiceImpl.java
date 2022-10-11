package com.nanum.webfluxservice.chat.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.Duration;

@Service
@Slf4j
@RequiredArgsConstructor
public class SSeServiceImpl {


    public <T> Flux<ServerSentEvent<T>> wrap(Flux<T> flux) {
        return Flux.merge(flux.map(t -> ServerSentEvent.builder(t).build()), Flux.interval(Duration.ofSeconds(15)).map(aLong -> ServerSentEvent.<T>builder().comment("keep alive").build()));
    }
}
