package com.example.batch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.example")
@EnableJpaRepositories(basePackages = "com.example") // or "com.example.domain"
@EntityScan(basePackages = "com.example")
public class BosinsaBatchApplication {

	public static void main(String[] args) {
		SpringApplication.run(BosinsaBatchApplication.class, args);
	}
}