package com.ifpe.userApi.util.enums;

public enum Role {
    LAWYER ("LAWYER"),
    CLIENT ("CLIENT"),
    EMPLOYEE ("EMPLOYEE");

    private final String role;

    Role (String role){
        this.role = role;
    }
    public String getRole() {
        return role;
    }
}
