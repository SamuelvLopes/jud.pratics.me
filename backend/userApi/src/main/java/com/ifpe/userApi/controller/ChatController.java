package com.ifpe.userApi.controller;


import com.ifpe.userApi.DTO.chat.ChatCreateDTO;
import com.ifpe.userApi.service.ChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/chat")
@Slf4j
public class ChatController {

    private final ChatService chatService;


    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }


    @PostMapping
    public ResponseEntity<Void> createChat(@RequestBody ChatCreateDTO chatCreateDTO) {
        log.info("ChatController :: createChat :: Received request to create chat.");
        URI location = chatService.create(chatCreateDTO);
        log.info("ChatController :: createChat :: Chat created successfully. Location: {}", location);
        return ResponseEntity.created(location).build();
    }
}
