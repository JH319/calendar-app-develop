package com.example.calendarappdevelop.user.service;

import com.example.calendarappdevelop.schedule.dto.GetScheduleResponse;
import com.example.calendarappdevelop.schedule.dto.UpdateScheduleRequest;
import com.example.calendarappdevelop.schedule.dto.UpdateScheduleResponse;
import com.example.calendarappdevelop.schedule.entity.Schedule;
import com.example.calendarappdevelop.user.dto.*;
import com.example.calendarappdevelop.user.entity.User;
import com.example.calendarappdevelop.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // 유저 생성
    @Transactional
    public CreateUserResponse save(CreateUserRequest request) {
        User user = new User(
                request.getUserName(),
                request.getEmail()
        );
        User savedUser = userRepository.save(user);
        return new CreateUserResponse(
                savedUser.getId(),
                savedUser.getUserName(),
                savedUser.getEmail(),
                savedUser.getCreatedAt(),
                savedUser.getModifiedAt()
        );
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
                () -> new IllegalStateException("존재하지 않는 유저입니다.")
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
    @Transactional
    public UpdateUserResponse updateUser(Long Id, UpdateUserRequest request) {
        // 유저 찾기
        User user = userRepository.findById(Id).orElseThrow(
                () -> new IllegalStateException("존재하지 않는 유저입니다.")
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
    @Transactional
    public void deleteUser(Long Id) {
        // 유저 존재 여부 확인
        boolean existence = userRepository.existsById(Id);

        // 유저가 없는 경우
        if(!existence) {
            throw new IllegalStateException("존재하지 않는 유저입니다.");
        }

        // 유저가 있는 경우 -> 삭제
        userRepository.deleteById(Id);
    }
}
