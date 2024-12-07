package com.ifpe.userApi.service;


import com.ifpe.userApi.DTO.chat.ChatCreateDTO;
import com.ifpe.userApi.DTO.user.UserCreateDTO;
import com.ifpe.userApi.entities.Chat;
import com.ifpe.userApi.entities.User;
import com.ifpe.userApi.exceptions.ChatCreationException;
import com.ifpe.userApi.exceptions.UserCreationException;
import com.ifpe.userApi.exceptions.UserNotFoundException;
import com.ifpe.userApi.repository.ChatRepository;
import com.ifpe.userApi.repository.UserRepository;
import com.ifpe.userApi.util.dto.DTOUtil;
import com.ifpe.userApi.util.encrypt.EncryptUtil;
import com.ifpe.userApi.util.entities.ChatUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;

@Service
@Slf4j
public class ChatService {

    private final ChatRepository chatRepository;
    private final UserRepository userRepository;

    public ChatService(ChatRepository chatRepository, UserRepository userRepository) {
        this.chatRepository = chatRepository;
        this.userRepository = userRepository;
    }


    @Transactional
    public URI create(ChatCreateDTO data) {
        log.info("ChatService :: create :: Starting chat creation process...");

        DTOUtil.validateChatCreateDTO(data);

        Chat chat = new Chat();

        try {
            User firstUser = userRepository.findByUniqueToken(EncryptUtil.decrypt(data.firstId()))
                    .orElseThrow(
                            () -> new UserNotFoundException("User not found for uniqueToken: " + data.firstId()));
            log.info("ChatService ::  :: ");

            User secondUser = userRepository.findByUniqueToken(EncryptUtil.decrypt(data.secondId()))
                    .orElseThrow(
                            () -> new UserNotFoundException("User not found for uniqueToken: " + data.secondId())
                    );
            log.info("ChatService ::  :: ");
            if (firstUser == null || secondUser == null) {
                throw new ChatCreationException("User id not found");
            }
            chat.getUsers().add(firstUser);
            chat.getUsers().add(secondUser);

            log.info("ChatService ::  :: ");

            chat.setCreationDate(LocalDate.now());

            chat.setUniqueToken(
                    ChatUtils.generateUniqueChatToken(
                            firstUser.getUniqueToken()
                            , secondUser.getUniqueToken()
                            , chat.getCreationDate()
                    )
            );
            chat = chatRepository.save(chat);

            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(DTOUtil.encryptUniqueToken(chat.getUniqueToken()))
                    .toUri();

            log.info("ChatService :: create ::  : {}");
            return location;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

}
