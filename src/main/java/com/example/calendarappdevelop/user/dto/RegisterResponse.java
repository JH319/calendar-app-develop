package com.example.calendarappdevelop.user.dto;

import lombok.Getter;

@Getter
public class RegisterResponse {
    private final Long userId;

    public RegisterResponse(Long userId) {
        this.userId = userId;
    }
}
