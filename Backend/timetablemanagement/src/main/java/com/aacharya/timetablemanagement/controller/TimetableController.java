package com.aacharya.timetablemanagement.controller;

import com.aacharya.timetablemanagement.dto.TimetableRequestDTO;
import com.aacharya.timetablemanagement.dto.TimetableResponseDTO;
import com.aacharya.timetablemanagement.service.TimetableService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@Tag(name = "Timetable Controller", description = "APIs for managing timetables")
@RestController
@RequestMapping("/api/timetables")
public class TimetableController {

    @Autowired
    private TimetableService timetableService;


    @Operation(summary = "Create a new timetable",
    description = "Creates a new timetable and stores it in the system."
            )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Timetable created successfully"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Teacher, Batch or Subject not found"
            )
    })
    @PostMapping
    public ResponseEntity<TimetableResponseDTO> saveTimetable(
            @Valid @RequestBody TimetableRequestDTO requestDTO) {

        TimetableResponseDTO response = timetableService.saveTimetable(requestDTO);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @Operation(
            summary = "Get all timetables",
            description = "Returns the complete list of timetables available in the system."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "All timetables retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<List<TimetableResponseDTO>> getAllTimetables() {

        List<TimetableResponseDTO> timetables = timetableService.getAllTimetables();

        return ResponseEntity.status(HttpStatus.OK)
                .body(timetables);
    }

    @Operation(
            summary = "Get timetable by ID",
             description = "Returns the timetable corresponding to the specified timetable ID."
            )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Timetable retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Timetable not found for the specified ID.")
    })
    @GetMapping("/{id}")
    public ResponseEntity<TimetableResponseDTO> getTimetableById(
            @PathVariable Long id) {

        TimetableResponseDTO timetable =
                timetableService.getTimetableById(id);

        return ResponseEntity.status(HttpStatus.OK)
                .body(timetable);
    }


    @Operation(
            summary = "Update timetable",
            description = "Updates the timetable corresponding to the specified timetable ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Timetable updated successfully"),
            @ApiResponse(responseCode = "404", description = "Timetable not found for the specified ID"),
            @ApiResponse(responseCode = "400", description = "Invalid timetable data")
    })
    @PutMapping("/{id}")
    public ResponseEntity<TimetableResponseDTO> updateTimetable(
            @PathVariable Long id,
            @Valid @RequestBody TimetableRequestDTO requestDTO) {

        TimetableResponseDTO response =
                timetableService.updateTimetable(id, requestDTO);

        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }


    @Operation(
            summary = "Delete timetable by ID",
            description = "Deletes the timetable corresponding to the specified timetable ID."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Timetable deleted successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Timetable not found for the specified ID"
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTimetable(
            @PathVariable Long id) {

        timetableService.deleteTimetable(id);

        return ResponseEntity.noContent().build();
    }

}
