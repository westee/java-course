package com.github.westee.course;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class CourseApplication {

	public static void main(String[] args) {
		SpringApplication.run(CourseApplication.class, args);
	}

}
