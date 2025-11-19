package com.example.calendarappdevelop.schedule.service;

import com.example.calendarappdevelop.schedule.dto.*;
import com.example.calendarappdevelop.common.entity.Schedule;
import com.example.calendarappdevelop.schedule.repository.ScheduleRepository;
import com.example.calendarappdevelop.common.exception.CustomException;
import com.example.calendarappdevelop.common.exception.ErrorMessage;
import com.example.calendarappdevelop.user.dto.SessionUser;
import com.example.calendarappdevelop.common.entity.User;
import com.example.calendarappdevelop.user.repository.UserRepository;
import com.example.calendarappdevelop.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    // 일정 생성
    public CreateScheduleResponse save(SessionUser sessionUser, CreateScheduleRequest request) {
        // 1. 유저 아이디로 User Entity 조회
        User user = userRepository.findById(sessionUser.getId()).orElseThrow(
                () -> new CustomException(ErrorMessage.NOT_FOUND_USER)
        );

        // 2. 요청받은 DTO를 Entity로 변환
        Schedule schedule = new Schedule(
                user,
                request.getTitle(),
                request.getContent()
        );

        // 3. 변환된 Entity를 DB에 저장
        Schedule savedSchedule = scheduleRepository.save(schedule);

        // 4. 저장된 Entity를 Controller에 반환
        return new CreateScheduleResponse(
                savedSchedule.getScheduleId(),
                savedSchedule.getUser().getId(),
                savedSchedule.getUser().getUserName(),
                savedSchedule.getTitle(),
                savedSchedule.getContent(),
                savedSchedule.getCreatedAt(),
                savedSchedule.getModifiedAt()
        );
    }

    // 전체 일정 조회
    @Transactional(readOnly = true)
    public List<GetScheduleResponse> findSchedules(Long userId) {
        List<Schedule> schedules;
        // 작성자명이 있을 때
        if (userId != null) {
            schedules = scheduleRepository.findScheduleByUserId(userId);
        }
        // 작성자명이 없을 때
        else {
            schedules = scheduleRepository.findAllByOrderByModifiedAtDesc();
        }
        List<GetScheduleResponse> dtos = new ArrayList<>();
        for (Schedule schedule : schedules) {
            GetScheduleResponse dto = new GetScheduleResponse(
                    schedule.getScheduleId(),
                    schedule.getUser().getId(),
                    schedule.getUser().getUserName(),
                    schedule.getTitle(),
                    schedule.getContent(),
                    schedule.getCreatedAt(),
                    schedule.getModifiedAt()
            );
            dtos.add(dto);
        }
        return dtos;
    }

    // 선택 일정 조회
    @Transactional(readOnly = true)
    public GetScheduleResponse findOne(Long Id) {
        Schedule schedule = scheduleRepository.findById(Id).orElseThrow(
                () -> new CustomException(ErrorMessage.NOT_FOUND_POST)
        );
        return new GetScheduleResponse(
                schedule.getScheduleId(),
                schedule.getUser().getId(),
                schedule.getUser().getUserName(),
                schedule.getTitle(),
                schedule.getContent(),
                schedule.getCreatedAt(),
                schedule.getModifiedAt()
        );
    }

    // 일정 수정
    public UpdateScheduleResponse updateSchedule(SessionUser sessionUser, Long Id, UpdateScheduleRequest request) {
        // 1. Id로 일정 조회
        Schedule schedule = scheduleRepository.findById(Id).orElseThrow(
                () -> new CustomException(ErrorMessage.NOT_FOUND_POST)
        );

        // 2. SessionUser의 Id와 일정 작성한 User의 Id가 같은지 확인
        userService.sessionMatch(sessionUser, schedule.getUser().getId());

        // 3. Entity 변경하여 DB에 반영
        schedule.update(
                request.getTitle(),
                request.getContent()
        );
        return new UpdateScheduleResponse(
                schedule.getScheduleId(),
                schedule.getUser().getId(),
                schedule.getUser().getUserName(),
                schedule.getTitle(),
                schedule.getContent(),
                schedule.getCreatedAt(),
                schedule.getModifiedAt()
        );
    }

    // 일정 삭제
    public void delete(SessionUser sessionUser, Long Id) {
        // 일정 존재 여부 확인
        Schedule schedule = scheduleRepository.findById(Id).orElseThrow(
                () -> new CustomException(ErrorMessage.NOT_FOUND_POST));

        userService.sessionMatch(sessionUser, schedule.getUser().getId());

        // 일정이 있는 경우 -> 삭제
        scheduleRepository.deleteById(Id);
    }
}
