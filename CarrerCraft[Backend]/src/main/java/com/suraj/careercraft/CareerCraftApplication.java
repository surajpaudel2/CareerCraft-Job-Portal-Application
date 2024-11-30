package com.suraj.careercraft;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CareerCraftApplication {

    public static void main(String[] args) {
        SpringApplication.run(CareerCraftApplication.class, args);
    }

}
