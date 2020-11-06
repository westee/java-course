package com.github.westee.course;

import com.github.westee.course.dao.UserRepository;
import com.github.westee.course.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@SpringBootApplication
@EnableJpaAuditing
public class CourseApplication {
	@Autowired
	UserRepository userRepository;

	public static void main(String[] args) {
		SpringApplication.run(CourseApplication.class, args);
	}

	@GetMapping("/xxx")
	public String xxx(){
		User user = new User();
		user.setEmail("xxx");
		user.setUsername("xxx");
		user.setEncrypted_password("xxx");
		userRepository.save(user);
		userRepository.findAll();
		return "ok";
	}
}
