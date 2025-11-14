package com.example.calendarappdevelop.schedule.controller;

import com.example.calendarappdevelop.schedule.dto.*;
import com.example.calendarappdevelop.schedule.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ScheduleController {
    private final ScheduleService scheduleService;

    // 일정 생성
    @PostMapping("schedules")
    public ResponseEntity<CreateScheduleResponse> createScheduleApi(@RequestBody CreateScheduleRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(scheduleService.save(request));
    }

    // 전체 일정 조회
    @GetMapping("/schedules")
    public ResponseEntity<List<GetScheduleResponse>> getSchedulesApi(@RequestParam(value = "author", required = false) String author) {
        List<GetScheduleResponse> result = scheduleService.findSchedules(author);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    // 선택 일정 조회
    @GetMapping("/schedules/{Id}")
    public ResponseEntity<GetScheduleResponse> getScheduleApi(@PathVariable Long Id) {
        return ResponseEntity.status(HttpStatus.OK).body(scheduleService.findOne(Id));
    }

    // 일정 수정
    @PutMapping("/schedules/{Id}")
    public ResponseEntity<UpdateScheduleResponse> updateScheduleApi(
            @PathVariable Long Id,
            @RequestBody UpdateScheduleRequest request) {

        UpdateScheduleResponse response = scheduleService.updateSchedule(Id, request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 일정 삭제
    @DeleteMapping("/schedules/{Id}")
    public ResponseEntity<Void> deleteScheduleApi(@PathVariable Long Id) {
        scheduleService.delete(Id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
