package com.example.calendarappdevelop.schedule.repository;

import com.example.calendarappdevelop.common.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findAllByOrderByModifiedAtDesc();
    List<Schedule> findScheduleByUserId(Long userId);
}
