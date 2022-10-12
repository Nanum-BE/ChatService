package com.nanum.webfluxservice.chat.presentaion;

import com.nanum.webfluxservice.chat.application.ChatService;
import com.nanum.webfluxservice.chat.dto.ChatDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chats")
public class ChatController {
    private final ChatService chatService;

//    @CrossOrigin
//    @GetMapping(value = "/sender/{sender}/receiver/{receiver}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
//    public Flux<ChatDto> getMsg(@PathVariable("sender") Long sender, @PathVariable("receiver") Long receiver){
//        return chatService.findBySenderAndReceiver(sender,receiver).subscribeOn(Schedulers.boundedElastic());
//    }
//
//    @CrossOrigin
//    @GetMapping(value = "/room/{roomId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
//    public Flux<ChatDto> chat(@PathVariable("roomId") Long roomId){
//        return chatService.connectedRoom(roomId).subscribeOn(Schedulers.boundedElastic());
//    }



    @CrossOrigin
    @PostMapping
    public Mono<ChatDto> setMsg(@RequestBody ChatDto chatDto){
        chatDto.setCreateAt(LocalDateTime.now());

        return chatService.chatSave(chatDto);
    }

}
