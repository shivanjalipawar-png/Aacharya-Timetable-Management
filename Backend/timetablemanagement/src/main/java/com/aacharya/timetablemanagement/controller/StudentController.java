
package com.aacharya.timetablemanagement.controller;

import com.aacharya.timetablemanagement.dto.StudentRequestDTO;
import com.aacharya.timetablemanagement.dto.StudentResponseDTO;
import com.aacharya.timetablemanagement.dto.StudentUpdateRequestDTO;
import com.aacharya.timetablemanagement.service.StudentService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    // Constructor injection
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    // Create a new student
    @PostMapping
    public ResponseEntity<StudentResponseDTO> createStudent(
            @Valid @RequestBody StudentRequestDTO request) {

        StudentResponseDTO response = studentService.createStudent(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Get all students
    @GetMapping
    public ResponseEntity<List<StudentResponseDTO>> getAllStudents() {

        List<StudentResponseDTO> students = studentService.getAllStudents();

        return ResponseEntity.ok(students);
    }

    // Get student by ID
    @GetMapping("/{studentId}")
    public ResponseEntity<StudentResponseDTO> getStudentById(
            @PathVariable Long studentId) {

        StudentResponseDTO student = studentService.getStudentById(studentId);

        return ResponseEntity.ok(student);
    }

    // Update student details (Admin only)
    @PutMapping("/{studentId}")
    public ResponseEntity<StudentResponseDTO> updateStudent(
            @PathVariable Long studentId,
            @Valid @RequestBody StudentUpdateRequestDTO request) {

        StudentResponseDTO updatedStudent =
                studentService.updateStudent(studentId, request);

        return ResponseEntity.ok(updatedStudent);
    }


    // Delete student by ID (Admin only)
    @DeleteMapping("/{studentId}")
    public ResponseEntity<Void> deleteStudent(
            @PathVariable Long studentId) {

        studentService.deleteStudent(studentId);

        return ResponseEntity.noContent().build();
    }
}
