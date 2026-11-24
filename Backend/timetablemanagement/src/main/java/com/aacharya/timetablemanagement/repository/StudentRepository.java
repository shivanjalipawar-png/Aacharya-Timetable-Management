
package com.aacharya.timetablemanagement.repository;

import com.aacharya.timetablemanagement.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {

    // Find a student profile using its linked user account ID
    Optional<Student> findByUser_UserId(int userId);

    // Check whether a student email already exists
    boolean existsByEmail(String email);
}
