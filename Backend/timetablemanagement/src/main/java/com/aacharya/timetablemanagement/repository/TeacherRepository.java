package com.aacharya.timetablemanagement.repository;

import com.aacharya.timetablemanagement.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {

}