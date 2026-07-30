package com.aacharya.timetablemanagement.service;


import com.aacharya.timetablemanagement.dto.BatchRequestDTO;
import com.aacharya.timetablemanagement.dto.BatchResponseDTO;
import com.aacharya.timetablemanagement.entity.Batch;
import com.aacharya.timetablemanagement.repository.BatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.aacharya.timetablemanagement.exception.ResourceNotFoundException;

import java.util.ArrayList;
import  java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
public class BatchService {

    @Autowired
    private BatchRepository batchRepository;
    private static final Logger logger =
            LoggerFactory.getLogger(BatchService.class);



    public BatchResponseDTO saveBatch(BatchRequestDTO requestDTO) {

         Batch batch=new Batch();
        batch.setBatchName(requestDTO.getBatchName());
        batch.setCourse(requestDTO.getCourse());


        logger.info("Saving batch: {}", batch.getBatchName());

        Batch savedBatch = batchRepository.save(batch);

        logger.info("Batch created successfully: {}", savedBatch.getBatchName());

        BatchResponseDTO response= new BatchResponseDTO();

        response.setBatchName(savedBatch.getBatchName());
        response.setCourse(savedBatch.getCourse());
        response.setBatchId(savedBatch.getBatchId());

        return response;
    }

    public List<BatchResponseDTO> getAllBatches() {

        logger.info("Fetching all batches");
        List<Batch> batches = batchRepository.findAll();
        List<BatchResponseDTO> responseDTOArrayList = new ArrayList<>();
        for(Batch batch :batches){
            BatchResponseDTO response= new BatchResponseDTO();

            response.setBatchName(batch.getBatchName());
            response.setCourse(batch.getCourse());
            response.setBatchId(batch.getBatchId());

            responseDTOArrayList.add(response);

        }
        logger.info("Retrieved {} batches", batches.size());

        return responseDTOArrayList;
    }





    public BatchResponseDTO getBatchById(Long id) {


        logger.info("Fetching batch with id: {}", id);
        Batch batch = batchRepository.findById(id).orElse(null);

        if (batch == null) {
            throw new ResourceNotFoundException("Batch not found with id: " + id);
        }
        logger.info("Retrieved batch: {}", batch.getBatchName());

        BatchResponseDTO response= new BatchResponseDTO();

        response.setBatchName(batch.getBatchName());
        response.setCourse(batch.getCourse());
        response.setBatchId(batch.getBatchId());



        return response;
    }



    public BatchResponseDTO updateBatch(Long id, BatchRequestDTO requestDTO ) {

        Batch batch = batchRepository.findById(id).orElse(null);


        if (batch == null) {
            throw new ResourceNotFoundException("Batch not found with id: " + id);
        }

        batch.setBatchName(requestDTO.getBatchName());
        batch.setCourse(requestDTO.getCourse());

        logger.info("Updating batch: {}", batch.getBatchName());
        Batch updatedBatch = batchRepository.save(batch);
        BatchResponseDTO response= new BatchResponseDTO();

        response.setBatchName(updatedBatch.getBatchName());
        response.setCourse(updatedBatch.getCourse());
        response.setBatchId(updatedBatch.getBatchId());

        logger.info("Batch updated successfully: {}", updatedBatch.getBatchName());

        return response;
    }

    public void deleteBatch(Long id) {


        Batch batch = batchRepository.findById(id).orElse(null);

        if (batch == null) {
            throw new ResourceNotFoundException("Batch not found with id: " + id);
        }
        logger.info("Deleting batch: {}", batch.getBatchName());

        batchRepository.delete(batch);
        logger.info("Batch deleted successfully: {}", batch.getBatchName());
    }
}
