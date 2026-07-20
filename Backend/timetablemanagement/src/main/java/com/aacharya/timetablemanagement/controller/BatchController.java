package com.aacharya.timetablemanagement.controller;

import com.aacharya.timetablemanagement.entity.Batch;
import com.aacharya.timetablemanagement.service.BatchService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/batches")
public class BatchController {

    @Autowired
    private BatchService batchService;

    @PostMapping
    public Batch saveBatch(@RequestBody Batch batch) {
        return batchService.saveBatch(batch);
    }

    @GetMapping
    public List<Batch> getAllBatches() {
        return batchService.getAllBatches();
    }

    @GetMapping("/{id}")
    public Batch getBatchById(@PathVariable Long id) {
        return batchService.getBatchById(id);
    }

    @PutMapping("/{id}")
    public Batch updateBatch(@PathVariable Long id,
                             @RequestBody Batch updatedBatch) {

        return batchService.updateBatch(id, updatedBatch);
    }

    @DeleteMapping("/{id}")
    public void deleteBatch(@PathVariable Long id) {
        batchService.deleteBatch(id);
    }
}