package com.example.sportsbook_application_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SportsBookBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(SportsBookBackendApplication.class, args);
    }

}
