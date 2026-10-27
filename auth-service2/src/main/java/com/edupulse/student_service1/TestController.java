package com.edupulse.student_service1;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/api/auth-test")
    public String test() {
        return "JWT authentication successful!";
    }
}