package com.example.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(
    scanBasePackages = {
        "com.example"
    }
)
public class BosinsaApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(BosinsaApiApplication.class, args);
    }
}
