//package com.nanum.webfluxservice.client;
//
//import com.nanum.webfluxservice.client.vo.UserDto;
//import com.nanum.webfluxservice.client.vo.UserResponse;
//import org.springframework.cloud.openfeign.FeignClient;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestParam;
//import reactivefeign.spring.config.ReactiveFeignClient;
//import reactor.core.publisher.Flux;
//import reactor.core.publisher.Mono;
//
//import java.util.List;
//
//@ReactiveFeignClient(name = "user-service")
//public interface ReactiveUserServiceClient{
//    @GetMapping(value = "/api/v1/users/{userId}", produces = "application/json")
//    Mono<UserResponse<UserDto>> getUserFlux(@PathVariable("userId") Long userId);
//
//    @GetMapping(value = "/api/v1/users/particular/flux", produces = "application/json")
//    Flux<UserResponse<UserDto>> getUsersByIdFlux(@RequestParam(value="param", required=false, defaultValue="")
//                                                 List<Long> params);
//
//    @GetMapping(value = "/api/v1/users/particular/mono", produces = "application/json")
//    Mono<UserResponse<List<UserDto>>> getUsersByIdMono(@RequestParam(value="param", required=false, defaultValue="")
//                                                 List<Long> params);
//
//}
