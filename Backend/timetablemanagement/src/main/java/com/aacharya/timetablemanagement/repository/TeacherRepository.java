package com.aacharya.timetablemanagement.repository;

import com.aacharya.timetablemanagement.entity.Teacher;
import com.aacharya.timetablemanagement.entity.Timetable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    List<Teacher> findBySpecialization(String specialization);

    @Query("""
    SELECT t
    FROM Teacher t
    WHERE
    (:specialization IS NULL OR t.specialization = :specialization)
    AND
    (:qualification IS NULL OR t.qualification = :qualification)
    AND
    (:batchId IS NULL OR t.batch.batchId = :batchId)
    
""")
    List<Teacher> filterTeachers(
            @Param("specialization") String specialization,
            @Param("qualification") String qualification,
            @Param("batchId") Long batchId
    );



}