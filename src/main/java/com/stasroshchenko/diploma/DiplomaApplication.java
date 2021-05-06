package com.stasroshchenko.diploma;

import com.stasroshchenko.diploma.auth.ApplicationUser;
import com.stasroshchenko.diploma.auth.ApplicationUserDaoJpa;
import com.stasroshchenko.diploma.auth.ApplicationUserService;
import com.stasroshchenko.diploma.security.ApplicationUserRole;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import static com.stasroshchenko.diploma.security.ApplicationUserRole.ADMIN;

@SpringBootApplication
public class DiplomaApplication {

	public static void main(String[] args) {
		SpringApplication.run(DiplomaApplication.class, args);
	}

	@Bean
	public CommandLineRunner admin(ApplicationUserService service) {
		return (args) -> {
			service.signUpUser(new ApplicationUser(
					ADMIN,
					"admin",
					"admin@gmail.com",
					"admin",
					true,
					true,
					true,
					true
			));
		};
	}

}
