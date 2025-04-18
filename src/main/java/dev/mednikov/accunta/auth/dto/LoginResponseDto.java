package dev.mednikov.accunta.auth.dto;

import java.util.UUID;

public class LoginResponseDto {

    private UUID userId;
    private String token;

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
