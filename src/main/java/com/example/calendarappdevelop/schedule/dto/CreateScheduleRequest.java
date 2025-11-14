package com.example.calendarappdevelop.schedule.dto;

import lombok.Getter;

@Getter
public class CreateScheduleRequest {
    private String author;
    private String title;
    private String content;
}
