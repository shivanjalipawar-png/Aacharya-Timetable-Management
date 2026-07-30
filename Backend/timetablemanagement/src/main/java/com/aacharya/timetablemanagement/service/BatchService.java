package com.aacharya.timetablemanagement.service;

//import com.aacharya.timetablemanagement.controller.BatchController;
import com.aacharya.timetablemanagement.entity.Batch;
import com.aacharya.timetablemanagement.repository.BatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.aacharya.timetablemanagement.exception.ResourceNotFoundException;
import  java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
public class BatchService {

    @Autowired
    private BatchRepository batchRepository;
    private static final Logger logger =
            LoggerFactory.getLogger(BatchService.class);


    public Batch saveBatch(Batch batch) {

        logger.info("Saving batch: {}", batch.getBatchName());

        Batch savedBatch = batchRepository.save(batch);

        logger.info("Batch created successfully: {}", savedBatch.getBatchName());

        return savedBatch;
    }

    public List<Batch> getAllBatches() {
        return batchRepository.findAll();
    }

    public Batch getBatchById(Long id) {

        Batch batch = batchRepository.findById(id).orElse(null);

        if (batch == null) {
            throw new ResourceNotFoundException("Batch not found with id: " + id);
        }

        return batch;
    }

    public Batch updateBatch(Long id, Batch updatedBatch) {

        Batch batch = batchRepository.findById(id).orElse(null);

        if (batch == null) {
            throw new ResourceNotFoundException("Batch not found with id: " + id);
        }

        batch.setBatchName(updatedBatch.getBatchName());

        return batchRepository.save(batch);
    }

    public void deleteBatch(Long id) {

        Batch batch = batchRepository.findById(id).orElse(null);

        if (batch == null) {
            throw new ResourceNotFoundException("Batch not found with id: " + id);
        }

        batchRepository.delete(batch);
    }
}
