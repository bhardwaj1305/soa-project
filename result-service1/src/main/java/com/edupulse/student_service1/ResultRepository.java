package com.edupulse.student_service1;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResultRepository extends JpaRepository<Result, Long> {

    List<Result> findByStudentId(Long studentId);

    List<Result> findByStudentIdAndSemester(Long studentId, String semester);
}