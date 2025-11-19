package com.example.calendarappdevelop.user.controller;

import com.example.calendarappdevelop.user.dto.*;
import com.example.calendarappdevelop.user.entity.User;
import com.example.calendarappdevelop.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<RegisterResponse> signup(@Valid @RequestBody RegisterRequest request) {
        User user = userService.create(request);
        RegisterResponse response = new RegisterResponse(user.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request, HttpSession session) {
        User user = userService.login(request);
        SessionUser sessionUser = new SessionUser(user);
        session.setAttribute("loginUser", sessionUser);

        LoginResponse response = new LoginResponse(user.getId(), user.getEmail());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@SessionAttribute(name = "loginUser", required = false) SessionUser sessionUser, HttpSession session) {
        if (sessionUser == null) {
            return ResponseEntity.badRequest().build();
        }
        session.invalidate();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // 전체 유저 조회
    @GetMapping("/users")
    public ResponseEntity<List<GetUserResponse>> getUsers(@RequestParam(value = "userName", required = false) String userName) {
        List<GetUserResponse> result = userService.findUsers(userName);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    // 선택 유저 조회
    @GetMapping("/users/{Id}")
    public ResponseEntity<GetUserResponse> getUser(@PathVariable Long Id) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.findOne(Id));
    }

    // 유저 수정
    @PutMapping("/users/{Id}")
    public ResponseEntity<UpdateUserResponse> updateUser(
            @PathVariable Long Id,
            @RequestBody UpdateUserRequest request) {

        UpdateUserResponse response = userService.updateUser(Id, request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 유저 삭제
    @DeleteMapping("/users/{Id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long Id) {
        userService.deleteUser(Id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
