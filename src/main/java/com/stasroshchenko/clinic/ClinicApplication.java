package com.stasroshchenko.clinic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Dental clinic application runner.
 * @author staffsterr2000
 * @version 1.0
 */
@SpringBootApplication
public class ClinicApplication {

	/**
	 * Main method.
	 * @param args args passed to method.
	 * @since 1.0
	 */
	public static void main(String[] args) {
		SpringApplication.run(ClinicApplication.class, args);
	}

}
