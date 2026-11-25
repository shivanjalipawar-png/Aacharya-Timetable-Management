
package com.aacharya.timetablemanagement.service;

import com.aacharya.timetablemanagement.dto.StudentRequestDTO;
import com.aacharya.timetablemanagement.dto.StudentResponseDTO;
import com.aacharya.timetablemanagement.dto.StudentUpdateRequestDTO;
import com.aacharya.timetablemanagement.entity.Batch;
import com.aacharya.timetablemanagement.entity.Role;
import com.aacharya.timetablemanagement.entity.Student;
import com.aacharya.timetablemanagement.entity.User;
import com.aacharya.timetablemanagement.repository.BatchRepository;
import com.aacharya.timetablemanagement.repository.StudentRepository;
import com.aacharya.timetablemanagement.repository.UserRepository;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final BatchRepository batchRepository;
    private final PasswordEncoder passwordEncoder;

    // Constructor injection
    public StudentService(StudentRepository studentRepository,
                          UserRepository userRepository,
                          BatchRepository batchRepository,
                          PasswordEncoder passwordEncoder) {

        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
        this.batchRepository = batchRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Create a student along with the linked login account
    @Transactional
    public StudentResponseDTO createStudent(StudentRequestDTO request) {

        // 1. Check whether username already exists
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Username already exists"
            );
        }

        // 2. Check whether student email already exists
        if (studentRepository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Student email already exists"
            );
        }

        // 3. Find the selected batch
        Batch batch = batchRepository.findById(request.getBatchId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Batch not found"
                ));

        // 4. Create the User login account
        User user = new User();

        user.setUsername(request.getUsername());

        // Encrypt password before saving
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // Every account created through this method is a STUDENT
        user.setRole(Role.STUDENT);

        // Save User first to generate userId
        User savedUser = userRepository.save(user);

        // 5. Create the Student profile
        Student student = new Student();

        student.setName(request.getName());
        student.setEmail(request.getEmail());
        student.setPhone(request.getPhone());

        // Link Student with User and Batch
        student.setUser(savedUser);
        student.setBatch(batch);

        // 6. Save Student profile
        Student savedStudent = studentRepository.save(student);

        // 7. Return response without password
        return convertToResponseDTO(savedStudent);
    }

    // Get all students
    @Transactional(readOnly = true)
    public List<StudentResponseDTO> getAllStudents() {

        List<Student> students = studentRepository.findAll();

        List<StudentResponseDTO> responseList = new ArrayList<>();

        for (Student student : students) {
            responseList.add(convertToResponseDTO(student));
        }

        return responseList;
    }

    // Get student by ID
    @Transactional(readOnly = true)
    public StudentResponseDTO getStudentById(Long studentId) {

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Student not found"
                ));

        return convertToResponseDTO(student);
    }

    // Update an existing student
    @Transactional
    public StudentResponseDTO updateStudent(
            Long studentId,
            StudentUpdateRequestDTO request) {

        // 1. Find the existing student
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Student not found"
                ));

        // 2. Check whether the email is already used by another student
        if (!student.getEmail().equals(request.getEmail())
                && studentRepository.existsByEmail(request.getEmail())) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Student email already exists"
            );
        }

        // 3. Find the selected batch
        Batch batch = batchRepository.findById(request.getBatchId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Batch not found"
                ));

        // 4. Update existing student details
        student.setName(request.getName());
        student.setEmail(request.getEmail());
        student.setPhone(request.getPhone());
        student.setBatch(batch);

        // 5. Save updated student and return response
        Student updatedStudent = studentRepository.save(student);

        return convertToResponseDTO(updatedStudent);
    }


    // Delete student and linked login account
    @Transactional
    public void deleteStudent(Long studentId) {

        // 1. Find student
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Student not found"
                ));

        // 2. Get linked User account
        User user = student.getUser();

        // 3. Delete Student profile first
        studentRepository.delete(student);

        // 4. Delete linked User account
        userRepository.delete(user);
    }


    // Convert Student entity into StudentResponseDTO
    private StudentResponseDTO convertToResponseDTO(Student student) {

        return new StudentResponseDTO(
                student.getStudentId(),
                student.getName(),
                student.getEmail(),
                student.getPhone(),
                student.getUser().getUsername(),
                student.getBatch().getBatchId(),
                student.getBatch().getBatchName()
        );
    }





}
