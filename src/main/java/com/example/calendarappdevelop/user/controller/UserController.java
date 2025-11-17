package com.example.calendarappdevelop.user.controller;

import com.example.calendarappdevelop.user.dto.*;
import com.example.calendarappdevelop.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    // 유저 생성
    @PostMapping("users")
    public ResponseEntity<CreateUserResponse> createUserApi(@RequestBody CreateUserRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(request));
    }

    // 전체 유저 조회
    @GetMapping("/users")
    public ResponseEntity<List<GetUserResponse>> getUsersApi(@RequestParam(value = "userName", required = false) String userName) {
        List<GetUserResponse> result = userService.findUsers(userName);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    // 선택 유저 조회
    @GetMapping("/users/{Id}")
    public ResponseEntity<GetUserResponse> getUserApi(@PathVariable Long Id) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.findOne(Id));
    }

    // 유저 수정
    @PutMapping("/users/{Id}")
    public ResponseEntity<UpdateUserResponse> updateUserApi(
            @PathVariable Long Id,
            @RequestBody UpdateUserRequest request) {

        UpdateUserResponse response = userService.updateUser(Id, request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 유저 삭제
    @DeleteMapping("/users/{Id}")
    public ResponseEntity<Void> deleteUserApi(@PathVariable Long Id) {
        userService.deleteUser(Id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
