package com.example.calendarappdevelop.schedule.dto.request;

import lombok.Getter;

@Getter
public class UpdateScheduleRequest {
    private Long userId;
    private String title;
    private String content;
}
