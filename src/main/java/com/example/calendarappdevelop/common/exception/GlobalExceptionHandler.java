package com.example.calendarappdevelop.common.exception;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
@AllArgsConstructor
public class GlobalExceptionHandler {

    // 유효성 검사 예외 처리 (HTTP 400 Bad Request)
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        // Validation 오류 메시지들을 조합하여 클라이언트에게 전달
        String errorMessage = e.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> fieldError.getDefaultMessage())
                .collect(Collectors.joining(" | ")); // 여러 오류를 하나의 문자열로 묶음

        log.error("ValidationException 발생: {}", errorMessage);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(
                        HttpStatus.BAD_REQUEST.value(),
                        "VALIDATION_ERROR",
                        errorMessage)); // 400 Bad Request 반환
    }

    // CustomException 처리
    @ExceptionHandler(value = CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
        log.error("CustomException 발생 {} : ", e.getMessage());

        return ResponseEntity
                .status(e.getErrorMessage().getStatus())
                //.status(500)
                .body(new ErrorResponse(e.getErrorMessage(), e.getMessage()));
    }
}
