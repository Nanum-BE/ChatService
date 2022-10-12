package com.nanum.webfluxservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.data.redis.core.ReactiveValueOperations;
import reactor.core.publisher.Flux;

@SpringBootTest
class WebfluxServiceApplicationTests {

    @Autowired
    private ReactiveStringRedisTemplate template;

    @Test
    void contextLoads() {
    }

}
