package com.example.calendarappdevelop.schedule.repository;

import com.example.calendarappdevelop.schedule.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findByAuthorOrderByModifiedAtDesc(String author);
    List<Schedule> findAllByOrderByModifiedAtDesc();
}
