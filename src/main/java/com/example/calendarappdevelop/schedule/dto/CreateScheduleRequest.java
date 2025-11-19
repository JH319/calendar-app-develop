package com.example.calendarappdevelop.schedule.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class CreateScheduleRequest {
    @NotBlank(message = "일정 제목은 필수입니다.")
    @Size(max = 20, message = "일정 제목은 20글자 이내여야 합니다.")
    private String title;

    @NotBlank(message = "일정 내용은 필수입니다.")
    private String content;
}
