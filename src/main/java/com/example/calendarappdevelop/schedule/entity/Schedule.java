package com.example.calendarappdevelop.schedule.entity;

import com.example.calendarappdevelop.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "schedules")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Schedule extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long scheduleId;
    @Column(length = 30, nullable = false)
    private String title;
    @Column(length = 200, nullable = false)
    private String content;

    // 단방향 관계
    @ManyToOne(fetch = FetchType.LAZY, optional = false)  // optional은 JPA에서 null 허용 여부
    @JoinColumn(name = "userId", nullable = false) // nullable은 DB에서 null 허용 여부
    private User user;

    public Schedule(User user, String title, String content) {
        this.user = user;
        this.title = title;
        this.content = content;
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

}
