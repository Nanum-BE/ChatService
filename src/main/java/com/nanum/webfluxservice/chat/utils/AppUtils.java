package com.nanum.webfluxservice.chat.utils;

import com.nanum.webfluxservice.alert.domain.Alert;
import com.nanum.webfluxservice.alert.dto.AlertDto;
import com.nanum.webfluxservice.chat.domain.Chat;
import com.nanum.webfluxservice.chat.dto.ChatDto;

public class AppUtils {
    public static Chat dtoToEntity(ChatDto chatDto){
        return Chat.builder()
                .id(chatDto.getId())
                .msg(chatDto.getMsg())
                .read(chatDto.isRead())
                .receiverId(chatDto.getReceiverId())
                .receiverName(chatDto.getReceiverName())
                .senderName(chatDto.getSenderName())
                .senderId(chatDto.getSenderId())
                .updateAt(chatDto.getUpdateAt())
                .deleteAt(chatDto.getDeleteAt())
                .createAt(chatDto.getCreateAt())
                .build();
    }
    public static ChatDto entityToDto(Chat chat){
        return ChatDto.builder()
                .id(chat.getId())
                .msg(chat.getMsg())
                .read(chat.isRead())
                .receiverId(chat.getReceiverId())
                .receiverName(chat.getReceiverName())
                .senderName(chat.getSenderName())
                .senderId(chat.getSenderId())
                .createAt(chat.getCreateAt())
                .deleteAt(chat.getDeleteAt())
                .updateAt(chat.getUpdateAt())
                .build();
    }
}
