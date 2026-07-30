package com.aacharya.timetablemanagement.controller;


import com.aacharya.timetablemanagement.dto.SubjectRequestDTO;
import com.aacharya.timetablemanagement.dto.SubjectResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.aacharya.timetablemanagement.service.SubjectService;
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
public ResponseEntity<SubjectResponseDTO> saveSubject(@RequestBody SubjectRequestDTO requestDTO){

    //Subject savedSubject = subjectService.save(subject);
        SubjectResponseDTO response = subjectService.save(requestDTO);

    return ResponseEntity.status(HttpStatus.CREATED)
            .body(response);
}

    @Operation(
            summary = "Get all subjects  data.",
            description = "Returns the complete list of subjects available in the system."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "All subjects retrieved successfully"),
    })


@GetMapping
    public ResponseEntity<List<SubjectResponseDTO>> getAllSubject() {
        List<SubjectResponseDTO> subjects = subjectService.getAllSubjects();

        return ResponseEntity.status(HttpStatus.OK)
                .body(subjects);
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
    public ResponseEntity<SubjectResponseDTO>getSubjectById(@PathVariable Long id){
    SubjectResponseDTO subjectResponse = subjectService.getSubjectById(id);

    return ResponseEntity.status(HttpStatus.OK)
            .body(subjectResponse);
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
    public ResponseEntity<SubjectResponseDTO> updateSubject(@PathVariable Long id,
                                                 @RequestBody SubjectRequestDTO requestDTO){

        SubjectResponseDTO updateSubject = subjectService.updateSubject(id, requestDTO);

        return ResponseEntity.status(HttpStatus.OK)
                .body(updateSubject);
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
