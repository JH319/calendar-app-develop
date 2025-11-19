package com.example.calendarappdevelop.user.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorMessage {

    NOT_FOUND_USER(HttpStatus.NOT_FOUND,"존재하지 않는 유저입니다."),
    NOT_FOUND_POST(HttpStatus.NOT_FOUND,"존재하지 않는 일정입니다."),
    DUPLICATE_EMAIL_ERROR(HttpStatus.CONFLICT, "이미 등록된 이메일입니다."),
    EMAIL_AUTH_FAIL(HttpStatus.UNAUTHORIZED, "이메일이 일치하지 않습니다."),
    PASSWORD_AUTH_FAIL(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다."),
    LOGIN_INFO_MISMATCH(HttpStatus.UNAUTHORIZED, "로그인정보가 일치하지 않습니다."),
    LOGIN_REQUIRED(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.")
    ;

    private final HttpStatus status;
    private final String message;

}
