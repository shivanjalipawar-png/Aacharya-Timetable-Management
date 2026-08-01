package com.aacharya.timetablemanagement.repository;

import com.aacharya.timetablemanagement.dto.TimetableResponseDTO;
import com.aacharya.timetablemanagement.entity.Timetable;
import com.aacharya.timetablemanagement.exception.ResourceNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.List;

public interface TimetableRepository extends JpaRepository<Timetable, Long>{

    List<Timetable> findByTeacher_TeacherId(Long teacherId);

    List<Timetable> findByBatch_BatchId(Long batchId);

    List<Timetable> findBySubject_SubjectId(Long subjectId);




}
