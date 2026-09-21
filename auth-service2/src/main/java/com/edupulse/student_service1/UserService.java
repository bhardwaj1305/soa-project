package com.edupulse.student_service1;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(String username,
                             String password,
                             String role,
                             Long studentId) {

        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        User user = new User();

        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role.toUpperCase());
        user.setStudentId(studentId);

        return userRepository.save(user);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() ->
                    new RuntimeException("User not found"));
    }

    public boolean checkPassword(String rawPassword,
                                 String encodedPassword) {

        return passwordEncoder.matches(
                rawPassword,
                encodedPassword
        );
    }
}