package com.nanum.webfluxservice.chat.config;

import com.nanum.webfluxservice.chat.application.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;

import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class ChatRoomSocketConfig {

    private final ChatRoomService chatRoomService;

    @Bean
    public HandlerMapping handlerMapping(){
        Map<String, WebSocketHandler> serviceMap = Map.of("/chat", chatRoomService);

        return new SimpleUrlHandlerMapping(serviceMap, -1);
    }
}
