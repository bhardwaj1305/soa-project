package com.edupulse.student_service1;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "student-service1",
        configuration = FeignConfig.class
)
public interface StudentClient {

    @GetMapping("/students/{id}")
    Student getStudentById(@PathVariable("id") Long id);
}