package com.aacharya.timetablemanagement.controller;

import com.aacharya.timetablemanagement.dto.BatchRequestDTO;
import com.aacharya.timetablemanagement.dto.BatchResponseDTO;
import com.aacharya.timetablemanagement.dto.SubjectRequestDTO;
import com.aacharya.timetablemanagement.dto.SubjectResponseDTO;
import com.aacharya.timetablemanagement.entity.Batch;
import com.aacharya.timetablemanagement.repository.BatchRepository;
import com.aacharya.timetablemanagement.service.BatchService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.List;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Tag(
        name = "Batch Management",
        description = "APIs for managing batches"
)
@RestController
@RequestMapping("/batches")
public class BatchController {

    @Autowired
    private BatchService batchService;
    private static final Logger logger =
            LoggerFactory.getLogger(BatchController.class);

    @Operation(
            summary = "Create a new batch",
            description = "Creates a new batch and stores it in the system."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Batch created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid batch data")
    })
    @PostMapping
    public ResponseEntity<BatchResponseDTO> saveBatch(@Valid @RequestBody BatchRequestDTO requestDTO) {

        BatchResponseDTO response = batchService.saveBatch(requestDTO);


        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @Operation(
            summary = "Get all batches",
            description = "Returns the complete list of batches available in the system."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "All batches retrieved successfully"),
    })
    @GetMapping
    public ResponseEntity<List<BatchResponseDTO>> getAllBatches() {

        List<BatchResponseDTO> batches =
                batchService.getAllBatches();
        return ResponseEntity.status(HttpStatus.OK)
                .body(batches);

    }


    @Operation(
            summary = "Get batch by ID",
            description = "Returns the batch corresponding to the specified batch ID."
    )

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Batch retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Batch not found for the specified ID.")
    })
    @GetMapping("/{id}")
    public ResponseEntity<BatchResponseDTO> getBatchById(@PathVariable Long id) {

        BatchResponseDTO response = batchService.getBatchById(id);

        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }


    @Operation(
            summary = "Update batch",
            description = "Updates the batch corresponding to the specified batch ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Batch updated successfully."),
            @ApiResponse(responseCode = "404", description = "Batch not found for the specified ID."),
            @ApiResponse(responseCode = "400", description = "Invalid batch data. ")
    })
    @PutMapping("/{id}")
    public ResponseEntity<BatchResponseDTO> updateBatch(@PathVariable Long id,
                                                        @RequestBody BatchRequestDTO requestDTO) {

        BatchResponseDTO updatedBatch = batchService.updateBatch(id, requestDTO);

        return ResponseEntity.status(HttpStatus.OK)
                .body(updatedBatch);
    }


    @Operation(
            summary = "Delete batch by ID",
            description = "Deletes the batch corresponding to the specified batch ID."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Batch deleted successfully."
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBatch(@PathVariable Long id) {

        batchService.deleteBatch(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }
}