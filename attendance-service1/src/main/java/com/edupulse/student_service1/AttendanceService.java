package com.edupulse.student_service1;


import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final StudentClient studentClient;

    public AttendanceService(
            AttendanceRepository attendanceRepository,
            StudentClient studentClient) {

        this.attendanceRepository = attendanceRepository;
        this.studentClient = studentClient;
    }

    // Mark attendance
    public Attendance markAttendance(Attendance attendance) {

        // Check whether student exists
        try {

            Student student = studentClient.getStudentById(
                    attendance.getStudentId()
            );

            if (student == null) {
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Student not found"
                );
            }

        } catch (FeignException.NotFound e) {

            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Student with ID " + attendance.getStudentId() + " not found"
            );
        }

        return attendanceRepository.save(attendance);
    }

    // Get all attendance
    public List<Attendance> getAllAttendance() {
        return attendanceRepository.findAll();
    }

    // Get attendance by student
    public List<Attendance> getStudentAttendance(Long studentId) {
        return attendanceRepository.findByStudentId(studentId);
    }

    // Calculate attendance percentage
    public double getAttendancePercentage(Long studentId) {

        List<Attendance> records =
                attendanceRepository.findByStudentId(studentId);

        if (records.isEmpty()) {
            return 0.0;
        }

        long presentCount = records.stream()
                .filter(a -> "PRESENT".equalsIgnoreCase(a.getStatus()))
                .count();

        return (presentCount * 100.0) / records.size();
    }
}