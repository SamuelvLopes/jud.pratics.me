package com.ifpe.userApi.util.entities;

import com.ifpe.userApi.util.encrypt.EncryptUtil;

import java.time.LocalDateTime;

public class UserUtils {

    public static String generateUniqueUserToken(String name, String cpf, String email) throws Exception {

        String concatenated = LocalDateTime.now() +
                "," +
                name +
                "," +
                cpf +
                "," +
                email;

        return EncryptUtil.encrypt(concatenated);
    }
}
