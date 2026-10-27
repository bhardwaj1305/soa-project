package com.edupulse.student_service1;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    // Mark attendance
    @PostMapping
    public ResponseEntity<Attendance> markAttendance(
            @RequestBody Attendance attendance) {

        return ResponseEntity.ok(
                attendanceService.markAttendance(attendance)
        );
    }

    // Get all attendance
    @GetMapping
    public ResponseEntity<List<Attendance>> getAllAttendance() {

        return ResponseEntity.ok(
                attendanceService.getAllAttendance()
        );
    }

    // Get student's attendance
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Attendance>> getStudentAttendance(
            @PathVariable Long studentId,
            Authentication authentication) {

        Jwt jwt = (Jwt) authentication.getPrincipal();

        String role = jwt.getClaimAsString("role");
        Long tokenStudentId = jwt.getClaim("studentId");

        // STUDENT can access only their own attendance
        if ("STUDENT".equalsIgnoreCase(role)
                && !studentId.equals(tokenStudentId)) {

            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Students can access only their own attendance"
            );
        }

        return ResponseEntity.ok(
                attendanceService.getStudentAttendance(studentId)
        );
    }

    // Attendance percentage
    @GetMapping("/student/{studentId}/percentage")
    public ResponseEntity<Double> getAttendancePercentage(
            @PathVariable Long studentId,
            Authentication authentication) {

        Jwt jwt = (Jwt) authentication.getPrincipal();

        String role = jwt.getClaimAsString("role");
        Long tokenStudentId = jwt.getClaim("studentId");

        // STUDENT can access only their own percentage
        if ("STUDENT".equalsIgnoreCase(role)
                && !studentId.equals(tokenStudentId)) {

            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Students can access only their own attendance percentage"
            );
        }

        return ResponseEntity.ok(
                attendanceService.getAttendancePercentage(studentId)
        );
    }
}