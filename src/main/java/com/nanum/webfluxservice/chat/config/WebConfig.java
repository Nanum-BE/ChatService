//package com.nanum.webfluxservice.chat.config;
//
//import com.nanum.webfluxservice.chat.handler.ChatWebSocketHandler;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.reactive.HandlerMapping;
//import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
//import org.springframework.web.reactive.socket.WebSocketHandler;
//import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@Configuration
//public class WebConfig {
//    @Bean
//    public HandlerMapping handlerMapping(){
//        Map<String, WebSocketHandler> map = new HashMap<>();
//        map.put("/path", new ChatWebSocketHandler());
//        int order = -1;
//
//        return new SimpleUrlHandlerMapping(map, order);
//    }
//
//    @Bean
//    public WebSocketHandlerAdapter handlerAdapter(){
//        return new WebSocketHandlerAdapter();
//    }
//}
