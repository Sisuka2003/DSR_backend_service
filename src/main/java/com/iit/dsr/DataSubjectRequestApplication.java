package com.iit.dsr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DataSubjectRequestApplication {

	public static void main(String[] args) {
		SpringApplication.run(DataSubjectRequestApplication.class, args);
	}

}
