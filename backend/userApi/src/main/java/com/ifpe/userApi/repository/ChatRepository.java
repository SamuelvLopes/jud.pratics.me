package com.ifpe.userApi.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ifpe.userApi.entities.Chat;

import java.util.Optional;

@Repository
public interface ChatRepository extends JpaRepository<Chat,Long> {

    Optional<Chat> findByUsersUniqueToken(Long firstUserId, Long SecondUserId);
}
