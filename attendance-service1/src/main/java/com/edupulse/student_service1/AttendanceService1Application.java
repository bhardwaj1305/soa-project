package com.edupulse.student_service1;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class AttendanceService1Application {

    public static void main(String[] args) {
        SpringApplication.run(AttendanceService1Application.class, args);
    }

}
