package com.example.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(
	scanBasePackages = {
		"com.example"
	}
)
@EnableFeignClients(basePackages = {
	"com.example.external.social"
})
public class BosinsaApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(BosinsaApiApplication.class, args);
	}
}
