package com.example.calendarappdevelop.user.service;

import com.example.calendarappdevelop.common.exception.CustomException;
import com.example.calendarappdevelop.common.exception.ErrorMessage;
import com.example.calendarappdevelop.user.dto.*;
import com.example.calendarappdevelop.common.entity.User;
import com.example.calendarappdevelop.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // 회원가입
    public User create(RegisterRequest request) {
        // 이미 등록된 이메일인지 확인
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new CustomException(ErrorMessage.DUPLICATE_EMAIL_ERROR);
        }

        User user = new User(
                request.getUserName(),
                request.getEmail(),
                request.getPassword()
        );
        return userRepository.save(user);
    }

    // 로그인
    public User login(LoginRequest request) {
        // 이메일로 유저 찾기
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(
                // 유저가 존재하지 않음 -> 401 예외 발생
                () -> new CustomException(ErrorMessage.EMAIL_AUTH_FAIL)
        );

        // 비밀번호 일치 확인
        if (!user.getPassword().equals(request.getPassword())) {
            // 비밀번호 불일치 -> 401 예외 발생
            throw new CustomException(ErrorMessage.PASSWORD_AUTH_FAIL);
        }

        // 성공 시 유저 반환
        return user;
    }

    // 전체 유저 조회
    @Transactional(readOnly = true)
    public List<GetUserResponse> findUsers(String userName) {
        List<User> users;
        // 유저 이름이 있을 때
        if (userName != null) {
            users = userRepository.findByUserNameOrderByModifiedAtDesc(userName);
        }
        // 유저 이름이 없을 때
        else {
            users = userRepository.findAllByOrderByModifiedAtDesc();
        }
        List<GetUserResponse> dtos = new ArrayList<>();
        for (User user : users) {
            GetUserResponse dto = new GetUserResponse(
                    user.getId(),
                    user.getUserName(),
                    user.getEmail(),
                    user.getCreatedAt(),
                    user.getModifiedAt()
            );
            dtos.add(dto);
        }
        return dtos;
    }

    // 선택 유저 조회
    @Transactional(readOnly = true)
    public GetUserResponse findOne(Long Id) {
        User user = userRepository.findById(Id).orElseThrow(
                () -> new CustomException(ErrorMessage.NOT_FOUND_USER)
        );
        return new GetUserResponse(
                user.getId(),
                user.getUserName(),
                user.getEmail(),
                user.getCreatedAt(),
                user.getModifiedAt()
        );
    }

    // 유저 수정
    public UpdateUserResponse updateUser(Long Id, UpdateUserRequest request) {
        // 유저 찾기
        User user = userRepository.findById(Id).orElseThrow(
                () -> new CustomException(ErrorMessage.NOT_FOUND_USER)
        );
        user.update(
                request.getUserName(),
                request.getEmail()
        );
        return new UpdateUserResponse(
                user.getId(),
                user.getUserName(),
                user.getEmail(),
                user.getCreatedAt(),
                user.getModifiedAt()
        );
    }

    // 유저 삭제
    public void deleteUser(Long Id) {
        // 유저 존재 여부 확인
        boolean existence = userRepository.existsById(Id);

        // 유저가 없는 경우
        if (!existence) {
            throw new CustomException(ErrorMessage.NOT_FOUND_USER);
        }

        // 유저가 있는 경우 -> 삭제
        userRepository.deleteById(Id);
    }

    public boolean sessionMatch(SessionUser sessionUser, Long Id) {
        Long Id2 = sessionUser.getId();
        if(Objects.equals(Id2, Id)) {
            return true;
        }
        throw new CustomException(ErrorMessage.LOGIN_INFO_MISMATCH);
    }
}
