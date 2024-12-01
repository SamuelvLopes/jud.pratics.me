package com.ifpe.userApi.util.enums;

public enum Status {
    ACTIVE("ACTIVE"),
    INACTIVE("INACTIVE")

    private final String status;
}




    private final String role;

    Role (String role){
        this.role = role;
    }
    public String getRole() {
        return role;
    }
}
