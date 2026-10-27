package com.edupulse.student_service1;

import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ResultService {

    private final ResultRepository resultRepository;
    private final StudentClient studentClient;

    public ResultService(
            ResultRepository resultRepository,
            StudentClient studentClient) {

        this.resultRepository = resultRepository;
        this.studentClient = studentClient;
    }

    // =========================
    // ADD RESULT
    // =========================
    public Result addResult(Result result) {

        // Validate student exists
        try {

            Student student = studentClient.getStudentById(
                    result.getStudentId()
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
                    "Student with ID "
                            + result.getStudentId()
                            + " not found"
            );
        }

        // Validate marks
        validateMarks(
                result.getMarks(),
                result.getMaxMarks()
        );

        // Calculate grade automatically
        result.setGrade(
                calculateGrade(
                        result.getMarks(),
                        result.getMaxMarks()
                )
        );

        return resultRepository.save(result);
    }


    // =========================
    // GET ALL RESULTS
    // =========================
    public List<Result> getAllResults() {

        return resultRepository.findAll();
    }


    // =========================
    // GET RESULT BY ID
    // =========================
    public Result getResultById(Long id) {

        return resultRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Result with ID "
                                        + id
                                        + " not found"
                        )
                );
    }


    // =========================
    // GET STUDENT RESULTS
    // =========================
    public List<Result> getStudentResults(Long studentId) {

        // Verify student exists
        try {

            Student student =
                    studentClient.getStudentById(studentId);

            if (student == null) {

                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Student not found"
                );
            }

        } catch (FeignException.NotFound e) {

            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Student with ID "
                            + studentId
                            + " not found"
            );
        }

        return resultRepository.findByStudentId(studentId);
    }


    // =========================
    // GET STUDENT RESULTS
    // BY SEMESTER
    // =========================
    public List<Result> getStudentResultsBySemester(
            Long studentId,
            String semester) {

        // Verify student exists
        try {

            Student student =
                    studentClient.getStudentById(studentId);

            if (student == null) {

                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Student not found"
                );
            }

        } catch (FeignException.NotFound e) {

            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Student with ID "
                            + studentId
                            + " not found"
            );
        }

        return resultRepository
                .findByStudentIdAndSemester(
                        studentId,
                        semester
                );
    }


    // =========================
    // UPDATE RESULT
    // =========================
    public Result updateResult(
            Long id,
            Result updatedResult) {

        Result existingResult =
                resultRepository.findById(id)
                        .orElseThrow(() ->
                                new ResponseStatusException(
                                        HttpStatus.NOT_FOUND,
                                        "Result with ID "
                                                + id
                                                + " not found"
                                )
                        );

        // Update fields
        existingResult.setSubject(
                updatedResult.getSubject()
        );

        existingResult.setMarks(
                updatedResult.getMarks()
        );

        existingResult.setMaxMarks(
                updatedResult.getMaxMarks()
        );

        existingResult.setAcademicYear(
                updatedResult.getAcademicYear()
        );

        existingResult.setSemester(
                updatedResult.getSemester()
        );

        // Validate marks
        validateMarks(
                existingResult.getMarks(),
                existingResult.getMaxMarks()
        );

        // Recalculate grade
        existingResult.setGrade(
                calculateGrade(
                        existingResult.getMarks(),
                        existingResult.getMaxMarks()
                )
        );

        return resultRepository.save(existingResult);
    }


    // =========================
    // DELETE RESULT
    // =========================
    public void deleteResult(Long id) {

        if (!resultRepository.existsById(id)) {

            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Result with ID "
                            + id
                            + " not found"
            );
        }

        resultRepository.deleteById(id);
    }


    // =========================
    // CALCULATE PERCENTAGE
    // =========================
    public double calculatePercentage(Long studentId) {

        List<Result> results =
                getStudentResults(studentId);

        if (results.isEmpty()) {
            return 0.0;
        }

        double totalMarks = results.stream()
                .mapToDouble(Result::getMarks)
                .sum();

        double totalMaxMarks = results.stream()
                .mapToDouble(Result::getMaxMarks)
                .sum();

        if (totalMaxMarks == 0) {
            return 0.0;
        }

        return (totalMarks / totalMaxMarks) * 100;
    }


    // =========================
    // VALIDATE MARKS
    // =========================
    private void validateMarks(
            Double marks,
            Double maxMarks) {

        if (marks == null || maxMarks == null) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Marks and maxMarks are required"
            );
        }

        if (marks < 0 || maxMarks <= 0) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid marks"
            );
        }

        if (marks > maxMarks) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Marks cannot be greater than maxMarks"
            );
        }
    }


    // =========================
    // CALCULATE GRADE
    // =========================
    private String calculateGrade(
            double marks,
            double maxMarks) {

        double percentage =
                (marks / maxMarks) * 100;

        if (percentage >= 90) {
            return "A+";
        } else if (percentage >= 80) {
            return "A";
        } else if (percentage >= 70) {
            return "B";
        } else if (percentage >= 60) {
            return "C";
        } else if (percentage >= 50) {
            return "D";
        } else if (percentage >= 40) {
            return "E";
        } else {
            return "F";
        }
    }
}