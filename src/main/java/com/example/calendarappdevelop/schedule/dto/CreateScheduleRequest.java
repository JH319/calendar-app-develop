package com.example.calendarappdevelop.schedule.dto;

import lombok.Getter;

@Getter
public class CreateScheduleRequest {
    private Long userId;
    private String title;
    private String content;
}
