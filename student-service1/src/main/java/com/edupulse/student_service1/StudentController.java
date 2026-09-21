package com.edupulse.student_service1;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<Student> addStudent(
            @RequestBody Student student) {

        Student savedStudent =
                studentService.addStudent(student);

        return ResponseEntity.ok(savedStudent);
    }

    // GET ALL
    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {

        return ResponseEntity.ok(
                studentService.getAllStudents()
        );
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(
            @PathVariable Long id,
            Authentication authentication) {

        Jwt jwt = (Jwt) authentication.getPrincipal();

        String role = jwt.getClaimAsString("role");
        Long tokenStudentId = jwt.getClaim("studentId");

        if ("STUDENT".equalsIgnoreCase(role)
                && !id.equals(tokenStudentId)) {

            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Students can access only their own profile"
            );
        }

        return studentService.getStudentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(
            @PathVariable Long id,
            @RequestBody Student student) {

        return ResponseEntity.ok(
                studentService.updateStudent(id, student)
        );
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStudent(
            @PathVariable Long id) {

        studentService.deleteStudent(id);

        return ResponseEntity.ok(
                "Student deleted successfully"
        );
    }
}