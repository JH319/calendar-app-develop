package com.example.calendarappdevelop.user.dto;

import com.example.calendarappdevelop.user.entity.User;
import lombok.Getter;

@Getter
public class SessionUser {
    private final Long id;
    private final String email;
    private String userName;

    public SessionUser(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.userName = user.getUserName();
    }
}
