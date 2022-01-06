package com.main.sellplatform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class SellplatformApplication {

	public static void main(String[] args) {
		SpringApplication.run(SellplatformApplication.class, args);
	}

}
