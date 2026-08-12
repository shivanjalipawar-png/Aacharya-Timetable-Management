package com.aacharya.timetablemanagement.repository;

import com.aacharya.timetablemanagement.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface  SubjectRepository  extends JpaRepository<Subject,Long> {
    List<Subject> findBySubjectName(String subjectName);
    List<Subject> findBySubjectCode(String subjectCode);

    @Query("""
    SELECT s
    FROM Subject s
    WHERE
    (:subjectName IS NULL OR s.subjectName = :subjectName)
    AND
    (:subjectCode IS NULL OR s.subjectCode = :subjectCode)
    AND
    (:credits IS NULL OR s.credits = :credits)
    AND
    (:teacherId IS NULL OR s.teacher.teacherId = :teacherId)
""")
    List<Subject> filterSubjects(
            @Param("subjectName") String subjectName,
            @Param("subjectCode") String subjectCode,
            @Param("credits") Integer credits,
            @Param("teacherId") Long teacherId
    );
}
