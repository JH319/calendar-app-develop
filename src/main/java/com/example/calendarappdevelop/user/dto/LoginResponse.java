package com.example.calendarappdevelop.user.dto;

import lombok.Getter;

@Getter
public class LoginResponse {
    private final Long userId;
    private final String email;

    public LoginResponse(Long userId, String email) {
        this.userId = userId;
        this.email = email;
    }
}
