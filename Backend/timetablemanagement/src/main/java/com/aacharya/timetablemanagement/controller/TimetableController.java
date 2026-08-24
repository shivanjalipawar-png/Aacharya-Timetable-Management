package com.aacharya.timetablemanagement.controller;

import com.aacharya.timetablemanagement.dto.TimetableRequestDTO;
import com.aacharya.timetablemanagement.dto.TimetableResponseDTO;
import com.aacharya.timetablemanagement.service.TimetableService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;


@Tag(name = "Timetable Controller", description = "APIs for managing timetables")
@RestController
@RequestMapping("/api/timetables")
@SecurityRequirement(name = "bearerAuth")
public class TimetableController {

    @Autowired
    private TimetableService timetableService;

    //==== method tos ave all timetables===

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


    //Fetch all timetables
    @Operation(
            summary = "Get all timetables",
            description = "Returns the complete list of timetables available in the system."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "All timetables retrieved successfully")
    })
    //Using pageable concept
    @GetMapping
    public ResponseEntity<Page<TimetableResponseDTO>> getAllTimetables(

            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "timetableId") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        Sort sort;

        if (direction.equalsIgnoreCase("asc")) {
            sort = Sort.by(sortBy).ascending();
        } else {
            sort = Sort.by(sortBy).descending();
        }

        Pageable pageable = PageRequest.of(page, size,sort);

        Page<TimetableResponseDTO> timetables =
                timetableService.getAllTimetables(pageable);

        return ResponseEntity.ok(timetables);
    }


//Method for get timetable by Id
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

//Get  timetable by teacherId
    @Operation(
            summary = "Get timetables by Teacher ID",
            description = "Returns all timetables corresponding to the specified teacher ID."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Timetables retrieved successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No timetable found for the specified teacher ID"
            )
    })
    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<List<TimetableResponseDTO>> getTimetableByTeacher(
            @PathVariable Long teacherId) {

        List<TimetableResponseDTO> timetables =
                timetableService.getTimetableByTeacher(teacherId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(timetables);
    }

    //Get timetable by batchId
    @Operation(
            summary = "Get timetables by Batch ID",
            description = "Returns all timetables corresponding to the specified batch ID."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Timetables retrieved successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No timetable found for the specified batch ID"
            )
    })
    @GetMapping("/batch/{batchId}")
    public ResponseEntity<List<TimetableResponseDTO>> getTimetableByBatch(
            @PathVariable Long batchId) {

        List<TimetableResponseDTO> timetables =
                timetableService.getTimetableByBatch(batchId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(timetables);
    }


    //Get timetable  by subject Id
    @Operation(
            summary = "Get timetables by Subject ID",
            description = "Returns all timetables corresponding to the specified subject ID."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Timetables retrieved successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No timetable found for the specified subject ID"
            )
    })
    @GetMapping("/subject/{subjectId}")
    public ResponseEntity<List<TimetableResponseDTO>> getTimetableBySubject(
            @PathVariable Long subjectId) {

        List<TimetableResponseDTO> timetables =
                timetableService.getTimetableBySubject(subjectId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(timetables);
    }
    //get timetable for Day
    @Operation(
            summary = "get a timetable for day",
           description="Return all the timetable scheduled for a day"
    )
@ApiResponses({
        @ApiResponse(responseCode = "200",description = "Timetable fetched successfully for a day"),
        @ApiResponse(responseCode = "404",description = "No timetable found for specific day")
})
    @GetMapping("/day/{day}")
    public ResponseEntity<List<TimetableResponseDTO>> getTimetableByDay(
            @PathVariable DayOfWeek day
            ){
       List<TimetableResponseDTO> timetables = timetableService.getTimetableByDay(day);
            return  ResponseEntity.status(HttpStatus.OK).body(timetables);
    }


    //get timetable for classroom
    @Operation(
            summary = "get a timetable for classroom",
            description="Return all the timetable scheduled for a classroom"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200",description = "Timetable fetched successfully for a classroom"),
            @ApiResponse(responseCode = "404",description = "No timetable found for specific classroom")
    })
    @GetMapping("/classroom/{classroom}")
    public ResponseEntity<List<TimetableResponseDTO>> getTimetableByClassroom(
            @PathVariable String classroom
    ){
        List<TimetableResponseDTO> timetables = timetableService.getTimetableByClassroom(classroom);
        return  ResponseEntity.status(HttpStatus.OK).body(timetables);
    }

    //=========
    // Pagination
    //==========
//Get timetable by Time range
@Operation(
        summary = "Get timetables by time range ",
        description = "Returns all timetables corresponding to the specified time range."
)
@ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Timetables retrieved successfully"
        ),
        @ApiResponse(
                responseCode = "404",
                description = "No timetable found for the time range"
        )
})
@GetMapping("/time-range/{startTime}/{endTime}")
public ResponseEntity<List<TimetableResponseDTO>> getTimetableByTimeRange(
        @PathVariable("startTime") LocalTime startTime,
        @PathVariable("endTime") LocalTime endTime) {

    List<TimetableResponseDTO> timetables =
            timetableService.getTimetableByTimeRange(startTime , endTime);

    return ResponseEntity.status(HttpStatus.OK)
            .body(timetables);
}

//get timetable for today=day
@Operation(
        summary = "Get today's timetable",
        description = "Returns all timetables scheduled for the current day."
)
@ApiResponses({
        @ApiResponse(responseCode = "200", description = "Today's timetable retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "No timetable found for today")
})
@GetMapping("/today")
public ResponseEntity<List<TimetableResponseDTO>> getTodayTimetable() {

    List<TimetableResponseDTO> timetables =
            timetableService.getTodayTimetable();

    return ResponseEntity.ok(timetables);
}

// Get timetable by teacher+day

@Operation(
        summary = "Get timetables by Teacher and Day",
        description = "Returns all timetables for a specific teacher on a specific day."
)
@ApiResponses({
        @ApiResponse(responseCode = "200", description = "Timetables retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "No timetable found for the specified teacher and day")
})
@GetMapping("/teacher/{teacherId}/day/{day}")
public ResponseEntity<List<TimetableResponseDTO>> getTimetableByTeacherAndDay(
        @PathVariable Long teacherId,
        @PathVariable DayOfWeek day) {

    List<TimetableResponseDTO> timetables =
            timetableService.getTimetableByTeacherAndDay(teacherId, day);

    return ResponseEntity.ok(timetables);
}

//get timetable by batch+day
@Operation(
        summary = "Get timetables by Batch and Day",
        description = "Returns all timetables for a specific batch on a specific day."
)
@ApiResponses({
        @ApiResponse(responseCode = "200", description = "Timetables retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "No timetable found for the specified batch and day")
})
@GetMapping("/batch/{batchId}/day/{day}")
public ResponseEntity<List<TimetableResponseDTO>> getTimetableByBatchAndDay(
        @PathVariable Long batchId,
        @PathVariable DayOfWeek day) {

    List<TimetableResponseDTO> timetables =
            timetableService.getTimetableByBatchAndDay(batchId, day);

    return ResponseEntity.ok(timetables);
}

//Dynamic API  testing
    //1. Filter by teacher + Batch +day

    @Operation(
            summary = "Filter timetables",
            description = "Returns timetables based on optional teacherId, batchId,day, subjectId and classroomfilters."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Filtered timetables retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "No timetable found for the given filters")
    })
    @GetMapping("/filter")
    public ResponseEntity<List<TimetableResponseDTO>> getFilteredTimetables(

            @RequestParam(required = false) Long teacherId,

            @RequestParam(required = false) Long batchId,

            @RequestParam(required = false) DayOfWeek day,

            @RequestParam(required=false) Long subjectId ,

            @RequestParam(required = false) String classroom){

        List<TimetableResponseDTO> timetables =
                timetableService.getFilteredTimetables(teacherId, batchId,day,subjectId,classroom );

        return ResponseEntity.ok(timetables);
    }






    //Update timetable
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

//Delete timetable
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

    public ResponseEntity<Void> deleteTimetable(@PathVariable Long id) {

        timetableService.deleteTimetable(id);

        return ResponseEntity.noContent().build();
    }
}
