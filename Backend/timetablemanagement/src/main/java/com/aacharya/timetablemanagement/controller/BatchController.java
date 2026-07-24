package com.aacharya.timetablemanagement.controller;

import com.aacharya.timetablemanagement.entity.Batch;
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

@Tag(
        name = "Batch Management",
        description = "APIs for managing batches"
)
@RestController
@RequestMapping("/batches")
public class BatchController {

    @Autowired
    private BatchService batchService;

    @Operation(
            summary = "Create a new batch",
            description = "Creates a new batch and stores it in the system."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Batch created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid batch data")
    })
    @PostMapping
    public ResponseEntity<Batch> saveBatch(@Valid @RequestBody Batch batch) {

        Batch savedBatch = batchService.saveBatch(batch);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(savedBatch);
    }
    @Operation(
            summary = "Get all batches",
            description = "Returns the complete list of batches available in the system."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "All batches retrieved successfully"),
    })
    @GetMapping
    public List<Batch> getAllBatches() {
        return batchService.getAllBatches();
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
    public Batch getBatchById(@PathVariable Long id) {
        return batchService.getBatchById(id);
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
    public Batch updateBatch(@PathVariable Long id,
                             @RequestBody Batch updatedBatch) {

        return batchService.updateBatch(id, updatedBatch);
    }


    @Operation(
            summary = "Delete batch by ID",
            description = "Deletes the batch corresponding to the specified batch ID."
    )
    @ApiResponse(
            responseCode="200",
            description="Batch deleted successfully."
    )
    @DeleteMapping("/{id}")
    public void deleteBatch(@PathVariable Long id) {
        batchService.deleteBatch(id);
    }
}