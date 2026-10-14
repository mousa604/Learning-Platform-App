package com.example.LearningPlatformApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class LearningPlatformAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(LearningPlatformAppApplication.class, args);
	}

}
