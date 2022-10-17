package com.nanum.webfluxservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.data.mongodb.config.EnableReactiveMongoAuditing;

@SpringBootApplication(scanBasePackages = "com.nanum")
@EnableReactiveMongoAuditing
@EnableEurekaClient
public class WebfluxServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebfluxServiceApplication.class, args);
    }

}
