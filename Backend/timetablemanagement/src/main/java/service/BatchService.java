package com.aacharya.timetablemanagement.service;

import com.aacharya.timetablemanagement.entity.Batch;
import com.aacharya.timetablemanagement.repository.BatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import  java.util.List;
@Service
public class BatchService {

    @Autowired
    private BatchRepository batchRepository;

    public Batch saveBatch(Batch batch) {
        return batchRepository.save(batch);
    }

    public List<Batch> getAllBatches() {
        return batchRepository.findAll();
    }

    public Batch getBatchById(Long id) {
        return batchRepository.findById(id).orElse(null);
    }

    public Batch updateBatch(Long id, Batch updatedBatch) {
        Batch batch = batchRepository.findById(id).orElse(null);

        if (batch != null) {

            batch.setBatchName(updatedBatch.getBatchName());

            return batchRepository.save(batch);
        }

        return null;
    }

    public void deleteBatch(Long id) {
        batchRepository.deleteById(id);
    }
}
