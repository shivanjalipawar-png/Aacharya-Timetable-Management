package com.aacharya.timetablemanagement.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.aacharya.timetablemanagement.service.SubjectService;
import com.aacharya.timetablemanagement.entity.Subject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.List;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@Tag(
        name = "Subject Management",
        description = "APIs for managing subjects."
)
@RestController
@RequestMapping("/subjects")
public class SubjectController {
@Autowired
    private  SubjectService subjectService;


    @Operation(
            summary = "Create a new subject",
            description = "Creates a new subject and stores it in the system."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Subject created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid subject data")
    })

@PostMapping
public ResponseEntity<Subject> saveSubject(@RequestBody Subject subject){

    Subject savedSubject = subjectService.save(subject);


    return ResponseEntity.status(HttpStatus.CREATED)
            .body(savedSubject);
}

    @Operation(
            summary = "Get all subjects  data.",
            description = "Returns the complete list of subjects available in the system."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "All subjects retrieved successfully"),
    })


@GetMapping
    public List<Subject> getAllSubject() {
    return subjectService.getAllSubjects();
}

    @Operation(
            summary = "Get subject by ID",
            description = "Returns the subject corresponding to the specified subject ID."
    )

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Subject retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Subject not found for the specified ID.")
    })

@GetMapping("/{id}")
    public ResponseEntity<Subject>getSubjectById(@PathVariable Long id){
    Subject subject = subjectService.getSubjectById(id);

    return ResponseEntity.status(HttpStatus.OK)
            .body(subject);
}
@Operation(
        summary="Update subject .",
        description = "Updates the subject corresponding to the specified subject ID."
)
@ApiResponses({
        @ApiResponse(responseCode = "200", description = "Subject updated successfully."),
        @ApiResponse(responseCode = "404", description = "Subject  not found for the specified ID."),
        @ApiResponse(responseCode = "400", description = "Invalid subject data. ")
})
    @PutMapping("/{id}")
    public ResponseEntity<Subject> updateSubject(@PathVariable Long id,
                                                 @RequestBody Subject updateSubject){

        Subject subject = subjectService.updateSubject(id, updateSubject);

        return ResponseEntity.status(HttpStatus.OK)
                .body(subject);
    }

    @Operation(
            summary = "Delete subject by ID",
            description = "Deletes the subject corresponding to the specified subject ID."
    )
    @ApiResponse(
            responseCode = "204",
            description = "Subject deleted successfully."
    )

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubject(@PathVariable Long id){

        subjectService.deleteSubject(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }
}
