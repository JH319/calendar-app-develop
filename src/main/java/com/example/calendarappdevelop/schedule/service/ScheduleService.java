package com.example.calendarappdevelop.schedule.service;

import com.example.calendarappdevelop.schedule.dto.*;
import com.example.calendarappdevelop.schedule.entity.Schedule;
import com.example.calendarappdevelop.schedule.repository.ScheduleRepository;
import com.example.calendarappdevelop.user.entity.User;
import com.example.calendarappdevelop.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;

    // 일정 생성
    @Transactional
    public CreateScheduleResponse save(CreateScheduleRequest request) {
        User user = userRepository.findById(request.getUserId()).orElseThrow(
                () -> new IllegalStateException("존재하지 않는 유저입니다.")
        );
        Schedule schedule = new Schedule(
                user,
                request.getTitle(),
                request.getContent()
        );
        Schedule savedSchedule = scheduleRepository.save(schedule);
        return new CreateScheduleResponse(
                savedSchedule.getId(),
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
            schedules = scheduleRepository.findByUserIdOrderByModifiedAtDesc(userId);
        }
        // 작성자명이 없을 때
        else {
            schedules = scheduleRepository.findAllByOrderByModifiedAtDesc();
        }
        List<GetScheduleResponse> dtos = new ArrayList<>();
        for (Schedule schedule : schedules) {
            GetScheduleResponse dto = new GetScheduleResponse(
                    schedule.getId(),
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
                () -> new IllegalStateException("존재하지 않는 일정입니다.")
        );
        return new GetScheduleResponse(
                schedule.getId(),
                schedule.getUser().getId(),
                schedule.getUser().getUserName(),
                schedule.getTitle(),
                schedule.getContent(),
                schedule.getCreatedAt(),
                schedule.getModifiedAt()
        );
    }

    // 일정 수정
    @Transactional
    public UpdateScheduleResponse updateSchedule(Long Id, UpdateScheduleRequest request) {
        // 일정 찾기
        Schedule schedule = scheduleRepository.findById(Id).orElseThrow(
                () -> new IllegalStateException("존재하지 않는 일정입니다.")
        );

        schedule.update(
                request.getTitle(),
                request.getContent()
        );
        return new UpdateScheduleResponse(
                schedule.getId(),
                schedule.getUser().getId(),
                schedule.getUser().getUserName(),
                schedule.getTitle(),
                schedule.getContent(),
                schedule.getCreatedAt(),
                schedule.getModifiedAt()
        );
    }

    // 일정 삭제
    @Transactional
    public void delete(Long Id) {
        // 일정 존재 여부 확인
        boolean existence = scheduleRepository.existsById(Id);

        // 일정이 없는 경우
        if(!existence) {
            throw new IllegalStateException("존재하지 않는 일정입니다.");
        }

        // 일정이 있는 경우 -> 삭제
        scheduleRepository.deleteById(Id);
    }
}
