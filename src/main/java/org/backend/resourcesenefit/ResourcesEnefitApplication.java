package org.backend.resourcesenefit;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@Log4j2
@SpringBootApplication
@EnableScheduling
public class ResourcesEnefitApplication {

    public static void main(String[] args) {
        SpringApplication.run(ResourcesEnefitApplication.class, args);
    }

    @Bean
    ApplicationRunner applicationRunner() {
        return args -> log.info("Application started successfully");
    }

}
