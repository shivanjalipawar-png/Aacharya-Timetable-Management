package com.aacharya.timetablemanagement.repository;

import com.aacharya.timetablemanagement.entity.Batch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BatchRepository extends JpaRepository<Batch, Long> {

    List<Batch> findByCourse(String course);

    // Filter by batch name
    List<Batch> findByBatchName(String batchName);

    // Dynamic filtering
    @Query("""
        SELECT b
        FROM Batch b
        WHERE
        (:batchName IS NULL OR b.batchName = :batchName)
        AND
        (:course IS NULL OR b.course = :course)
    """)
    List<Batch> filterBatches(
            @Param("batchName") String batchName,
            @Param("course") String course
    );


}
