package com.example.calendarappdevelop.user.config;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

    private final ErrorMessage errorMessage;

    public CustomException(ErrorMessage errorMessage) {
        super(errorMessage.getMessage());
        this.errorMessage = errorMessage;
    }
}

