package com.example.LearningPlatformApp.repository;

import com.example.LearningPlatformApp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository  extends JpaRepository<User,Long> {
    long count();
}
