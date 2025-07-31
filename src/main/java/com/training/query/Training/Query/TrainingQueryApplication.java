package com.training.query.Training.Query;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

@SpringBootApplication(scanBasePackages = "com.training.query.Training.Query")
//@EnableScheduling
//@ComponentScan(basePackages = "com.training.query")
public class TrainingQueryApplication {
	public static void main(String[] args) {
		SpringApplication.run(TrainingQueryApplication.class, args);
	}
}

