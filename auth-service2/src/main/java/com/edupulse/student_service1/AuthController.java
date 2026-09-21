package com.edupulse.student_service1;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;

    public AuthController(UserService userService,
                          JwtService jwtService) {

        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(
            @RequestBody RegisterRequest request) {

        User user = userService.registerUser(
                request.getUsername(),
                request.getPassword(),
                request.getRole(),
                request.getStudentId()
        );

        return ResponseEntity.ok(user);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody LoginRequest request) {

        User user = userService.findByUsername(
                request.getUsername()
        );

        if (!userService.checkPassword(
                request.getPassword(),
                user.getPassword())) {

            throw new RuntimeException(
                    "Invalid username or password"
            );
        }

        String token = jwtService.generateToken(user);

        return ResponseEntity.ok(
                new LoginResponse(
                        "Login successful",
                        token,
                        user.getUsername(),
                        user.getRole()
                )
        );
    }

    // Register Request
    public static class RegisterRequest {

        private String username;
        private String password;
        private String role;
        private Long studentId;

        public RegisterRequest() {
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public Long getStudentId() {
            return studentId;
        }

        public void setStudentId(Long studentId) {
            this.studentId = studentId;
        }
    }

    // Login Request
    public static class LoginRequest {

        private String username;
        private String password;

        public LoginRequest() {
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    // Login Response
    public static class LoginResponse {

        private String message;
        private String token;
        private String username;
        private String role;

        public LoginResponse(String message,
                             String token,
                             String username,
                             String role) {

            this.message = message;
            this.token = token;
            this.username = username;
            this.role = role;
        }

        public String getMessage() {
            return message;
        }

        public String getToken() {
            return token;
        }

        public String getUsername() {
            return username;
        }

        public String getRole() {
            return role;
        }
    }
}