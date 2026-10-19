package com.server.mentorgrowth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class MentorGrowthApplication {

    public static void main(String[] args) {
        SpringApplication.run(MentorGrowthApplication.class, args);
    }
}
