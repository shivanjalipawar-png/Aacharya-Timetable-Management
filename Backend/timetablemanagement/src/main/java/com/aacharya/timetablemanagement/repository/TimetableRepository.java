package com.aacharya.timetablemanagement.repository;


import com.aacharya.timetablemanagement.entity.Timetable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

public interface TimetableRepository extends JpaRepository<Timetable, Long>{

    List<Timetable> findByTeacher_TeacherId(Long teacherId);

    List<Timetable> findByBatch_BatchId(Long batchId);

    List<Timetable> findBySubject_SubjectId(Long subjectId);

    @Query("""
SELECT t FROM Timetable t
WHERE t.batch.batchId = :batchId
AND t.day = :day
AND (
        :startTime < t.endTime
    AND :endTime > t.startTime
)
""")
    List<Timetable> findBatchConflicts(
            @Param("batchId") Long batchId,
            @Param("day") DayOfWeek day,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime
    );

    @Query("""
SELECT t FROM Timetable t
WHERE t.teacher.teacherId = :teacherId
AND t.day = :day
AND (
        :startTime < t.endTime
    AND :endTime > t.startTime
)
""")
    List<Timetable> findTeacherConflicts(
            @Param("teacherId") Long teacherId,
            @Param("day") DayOfWeek day,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime
    );

    //
    @Query("""
SELECT t FROM Timetable t
WHERE t.classroom = :classroom
AND t.day = :day
AND (
       :startTime < t.endTime
   AND :endTime > t.startTime
)
""")
    List<Timetable> findClassroomConflicts(
            @Param("classroom") String classroom,
            @Param("day") DayOfWeek day,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime
    );


}
