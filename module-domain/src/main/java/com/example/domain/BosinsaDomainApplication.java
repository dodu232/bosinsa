package com.example.domain;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication(
    scanBasePackages = {
        "com.example"
    }
)

@EnableJpaAuditing
public class BosinsaDomainApplication {
    public static void main(String[] args) {
        SpringApplication.run(BosinsaDomainApplication.class, args);
    }
}
