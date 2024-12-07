package com.ifpe.userApi.service;


import com.ifpe.userApi.repository.ChatRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class ChatService {


    private final PasswordEncoder passwordEncoder;
    private final ChatRepository chatRepository;

    public ChatService(PasswordEncoder passwordEncoder, ChatRepository chatRepository) {
        this.passwordEncoder = passwordEncoder;
        this.chatRepository = chatRepository;
    }

    @Transactional
    public
}
