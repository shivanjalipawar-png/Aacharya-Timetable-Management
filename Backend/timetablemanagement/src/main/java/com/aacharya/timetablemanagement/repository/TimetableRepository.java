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

    List<Timetable> findByStartTime(LocalTime startTime);

    List<Timetable> findByEndTime(LocalTime endTime);

    List<Timetable> findByStartTimeAndEndTime(
            LocalTime startTime,
            LocalTime endTime
    );

    List<Timetable> findByDay(DayOfWeek day);
    List<Timetable> findByClassroom(String classroom);
    List<Timetable> findByTeacher_TeacherIdAndDay(
            Long teacherId,
            DayOfWeek day
    );
    List<Timetable> findByBatch_BatchIdAndDay(
            Long batchId,
            DayOfWeek day
    );






    //JPQL for  save batch conflict
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

    //JPQL for  update batch conflict
    @Query("""
SELECT t FROM Timetable t
WHERE t.batch.batchId = :batchId
AND t.day = :day
AND (
        :startTime < t.endTime
    AND :endTime > t.startTime
)
AND t.timetableId <>:timetableId
""")
    List<Timetable> updateBatchConflicts(
            @Param("batchId") Long batchId,
            @Param("day") DayOfWeek day,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime,
            @Param("timetableId") Long timetableId
    );




    //===JPQL for save  teacher conflict====
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

    //===JPQL for update teacher conflict====
    @Query("""
SELECT t FROM Timetable t
WHERE t.teacher.teacherId = :teacherId
AND t.day = :day
AND (
        :startTime < t.endTime
    AND :endTime > t.startTime
   
)
  AND t.timetableId <>:timetableId
""")
    List<Timetable> updateTeacherConflicts(
            @Param("teacherId") Long teacherId,
            @Param("day") DayOfWeek day,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime,
            @Param("timetableId") Long timetableId

    );

    // JPQL for save classroom conflict
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


    // JPQL for update classroom conflict
    @Query("""
SELECT t FROM Timetable t
WHERE t.classroom = :classroom
AND t.day = :day
AND (
       :startTime < t.endTime
   AND :endTime > t.startTime
)
   AND t.timetableId <> :timetableId
  
""")
    List<Timetable> updateClassroomConflicts(
            @Param("classroom") String classroom,
            @Param("day") DayOfWeek day,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime,
            @Param("timetableId") Long timetableId
    );

    // JPQL query for fetching timetable by time range
    @Query("""
SELECT t FROM Timetable t
WHERE (
       :startTime < t.endTime
   AND :endTime > t.startTime
)
""")
    List<Timetable> findTimetableByTimeRange(
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime
    );



}
