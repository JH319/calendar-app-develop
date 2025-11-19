package com.example.calendarappdevelop.schedule.controller;

import com.example.calendarappdevelop.schedule.dto.request.CreateScheduleRequest;
import com.example.calendarappdevelop.schedule.dto.request.UpdateScheduleRequest;
import com.example.calendarappdevelop.schedule.dto.response.CreateScheduleResponse;
import com.example.calendarappdevelop.schedule.dto.response.GetScheduleResponse;
import com.example.calendarappdevelop.schedule.dto.response.UpdateScheduleResponse;
import com.example.calendarappdevelop.schedule.service.ScheduleService;
import com.example.calendarappdevelop.common.exception.CustomException;
import com.example.calendarappdevelop.common.exception.ErrorMessage;
import com.example.calendarappdevelop.user.dto.SessionUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ScheduleController {
    private final ScheduleService scheduleService;

    // 일정 생성
    @PostMapping("schedules")
    public ResponseEntity<CreateScheduleResponse> createSchedule(
            @Valid
            @RequestBody CreateScheduleRequest request,
            @SessionAttribute(name = "loginUser", required = false) SessionUser sessionUser) {

        // 로그인 확인
        if (sessionUser == null) {
            throw new CustomException(ErrorMessage.LOGIN_REQUIRED);
        }
        CreateScheduleResponse result = scheduleService.save(sessionUser, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    // 전체 일정 조회
    @GetMapping("/schedules")
    public ResponseEntity<List<GetScheduleResponse>> getSchedules(@RequestParam(value = "userId", required = false) Long userId) {
        List<GetScheduleResponse> result = scheduleService.findSchedules(userId);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    // 선택 일정 조회
    @GetMapping("/schedules/{scheduleId}")
    public ResponseEntity<GetScheduleResponse> getSchedule(@PathVariable Long scheduleId) {
        return ResponseEntity.status(HttpStatus.OK).body(scheduleService.findOne(scheduleId));
    }

    // 일정 수정
    @PutMapping("/schedules/{scheduleId}")
    public ResponseEntity<UpdateScheduleResponse> updateSchedule(
            @Valid
            @PathVariable Long scheduleId,
            @RequestBody UpdateScheduleRequest request,
            @SessionAttribute(name = "loginUser", required = false) SessionUser sessionUser) {

        // 로그인 확인
        if (sessionUser == null) {
            throw new CustomException(ErrorMessage.LOGIN_REQUIRED);
        }
        UpdateScheduleResponse result = scheduleService.updateSchedule(sessionUser, scheduleId, request);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    // 일정 삭제
    @DeleteMapping("/schedules/{scheduleId}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Long scheduleId,
                                               @SessionAttribute(name = "loginUser", required = false) SessionUser sessionUser) {

        // 로그인 확인
        if (sessionUser == null) {
            throw new CustomException(ErrorMessage.LOGIN_REQUIRED);
        }

        scheduleService.delete(sessionUser, scheduleId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
