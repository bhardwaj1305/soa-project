package com.edupulse.student_service1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ResultService1Application {

    public static void main(String[] args) {
        SpringApplication.run(ResultService1Application.class, args);
    }

}