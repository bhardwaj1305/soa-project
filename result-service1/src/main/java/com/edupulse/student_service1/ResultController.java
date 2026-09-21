package com.edupulse.student_service1;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/results")
public class ResultController {

    private final ResultService resultService;

    public ResultController(ResultService resultService) {
        this.resultService = resultService;
    }


    // =========================
    // ADD RESULT
    // =========================
    @PostMapping
    public ResponseEntity<Result> addResult(
            @RequestBody Result result) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(resultService.addResult(result));
    }


    // =========================
    // GET ALL RESULTS
    // =========================
    @GetMapping
    public ResponseEntity<List<Result>> getAllResults() {

        return ResponseEntity.ok(
                resultService.getAllResults()
        );
    }


    // =========================
    // GET RESULT BY ID
    // =========================
    @GetMapping("/{id}")
    public ResponseEntity<Result> getResultById(
            @PathVariable Long id,
            Authentication authentication) {

        Result result = resultService.getResultById(id);

        checkResultOwnership(result, authentication);

        return ResponseEntity.ok(result);
    }


    // =========================
    // GET STUDENT RESULTS
    // =========================
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Result>> getStudentResults(
            @PathVariable Long studentId,
            Authentication authentication) {

        checkStudentOwnership(
                studentId,
                authentication
        );

        return ResponseEntity.ok(
                resultService.getStudentResults(studentId)
        );
    }


    // =========================
    // GET STUDENT RESULTS
    // BY SEMESTER
    // =========================
    @GetMapping("/student/{studentId}/semester/{semester}")
    public ResponseEntity<List<Result>> getStudentResultsBySemester(
            @PathVariable Long studentId,
            @PathVariable String semester,
            Authentication authentication) {

        checkStudentOwnership(
                studentId,
                authentication
        );

        return ResponseEntity.ok(
                resultService.getStudentResultsBySemester(
                        studentId,
                        semester
                )
        );
    }


    // =========================
    // GET STUDENT PERCENTAGE
    // =========================
    @GetMapping("/student/{studentId}/percentage")
    public ResponseEntity<Double> getStudentPercentage(
            @PathVariable Long studentId,
            Authentication authentication) {

        checkStudentOwnership(
                studentId,
                authentication
        );

        return ResponseEntity.ok(
                resultService.calculatePercentage(studentId)
        );
    }


    // =========================
    // UPDATE RESULT
    // =========================
    @PutMapping("/{id}")
    public ResponseEntity<Result> updateResult(
            @PathVariable Long id,
            @RequestBody Result result) {

        return ResponseEntity.ok(
                resultService.updateResult(
                        id,
                        result
                )
        );
    }


    // =========================
    // DELETE RESULT
    // =========================
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteResult(
            @PathVariable Long id) {

        resultService.deleteResult(id);

        return ResponseEntity.ok(
                "Result deleted successfully"
        );
    }


    // =========================
    // STUDENT OWNERSHIP CHECK
    // =========================
    private void checkStudentOwnership(
            Long requestedStudentId,
            Authentication authentication) {

        Jwt jwt = (Jwt) authentication.getPrincipal();

        String role =
                jwt.getClaimAsString("role");

        Long tokenStudentId =
                jwt.getClaim("studentId");

        // STUDENT can access only their own data
        if ("STUDENT".equalsIgnoreCase(role)
                && !requestedStudentId.equals(tokenStudentId)) {

            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Students can access only their own results"
            );
        }
    }


    // =========================
    // RESULT OWNERSHIP CHECK
    // =========================
    private void checkResultOwnership(
            Result result,
            Authentication authentication) {

        Jwt jwt = (Jwt) authentication.getPrincipal();

        String role =
                jwt.getClaimAsString("role");

        Long tokenStudentId =
                jwt.getClaim("studentId");

        // STUDENT can access only their own result
        if ("STUDENT".equalsIgnoreCase(role)
                && !result.getStudentId().equals(tokenStudentId)) {

            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Students can access only their own results"
            );
        }
    }
}