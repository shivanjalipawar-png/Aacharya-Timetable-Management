
package com.aacharya.timetablemanagement;

import com.aacharya.timetablemanagement.entity.User;
import com.aacharya.timetablemanagement.entity.Role;
import com.aacharya.timetablemanagement.repository.UserRepository;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class TimetablemanagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(TimetablemanagementApplication.class, args);
	}

}
