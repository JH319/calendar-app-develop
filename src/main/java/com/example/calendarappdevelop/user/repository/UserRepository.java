package com.example.calendarappdevelop.user.repository;

import com.example.calendarappdevelop.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByUserNameOrderByModifiedAtDesc(String userName);
    List<User> findAllByOrderByModifiedAtDesc();
    Optional<User> findByEmail(String email);
}
