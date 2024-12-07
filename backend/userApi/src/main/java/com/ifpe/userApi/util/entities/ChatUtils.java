package com.ifpe.userApi.util.entities;

import com.ifpe.userApi.util.encrypt.EncryptUtil;

import java.time.LocalDate;

public class ChatUtils {

    public static String generateUniqueChatToken(
            String firstUserUniqueToken, String secondUserUniqueToken, LocalDate creationDate ) throws Exception {

        String concatenated = creationDate.toString() +
                "," +
                firstUserUniqueToken +
                "," +
                secondUserUniqueToken;

        return EncryptUtil.encrypt(concatenated);
    }
}
