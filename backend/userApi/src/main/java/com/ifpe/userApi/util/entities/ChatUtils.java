package com.ifpe.userApi.util.entities;

import com.ifpe.userApi.entities.User;

import java.util.Set;
import java.util.stream.Collectors;

public class ChatUtils {

    private String generateUserTokensString(Set<User> users) {
        return users.stream()
                .sorted((u1, u2) -> Long.compare(u1.getId(), u2.getId()))
                .map(user -> user.getId().toString())
                .collect(Collectors.joining(","));
    }


}
