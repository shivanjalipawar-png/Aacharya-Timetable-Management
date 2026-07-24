package com.aacharya.timetablemanagement.controller;

import com.aacharya.timetablemanagement.entity.Teacher;
import com.aacharya.timetablemanagement.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;


@Tag(
        name = "Teacher Management",
        description = "APIs for managing teachers."
)
@RestController
@RequestMapping("/teachers")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    @Operation(
            summary = "Create a new teacher",
            description = "Creates a new teacher and stores it in the system."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Teacher created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid Teacher data")
    })

    @PostMapping
    public ResponseEntity<Teacher> saveTeacher(@RequestBody Teacher teacher) {

        Teacher savedTeacher = teacherService.saveTeacher(teacher);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(savedTeacher);
    }

    @Operation(
            summary = "Get all teachers  data.",
            description = "Returns the complete list of teachers available in the system."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "All teachers  retrieved successfully"),
    })

    @GetMapping
    public ResponseEntity<List<Teacher>> getAllTeachers() {

        return ResponseEntity.status(HttpStatus.OK)
                .body(teacherService.getAllTeachers());
    }


    @Operation(
            summary = "Get teacher by ID",
            description = "Returns the teacher corresponding to the specified teacher ID."
    )

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Teacher retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Teacher not found for the specified ID.")
    })

    @GetMapping("/{id}")
    public ResponseEntity<Teacher> getTeacherById(@PathVariable Long id) {

        Teacher teacher = teacherService.getTeacherById(id);

        return ResponseEntity.status(HttpStatus.OK)
                .body(teacher);
    }


    @Operation(
            summary = "Update teacher",
            description = "Updates the teacher corresponding to the specified teacher ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Teacher updated successfully."),
            @ApiResponse(responseCode = "404", description = "Teacher  not found for the specified ID."),
            @ApiResponse(responseCode = "400", description = "Invalid teacher data. ")
    })

    @PutMapping("/{id}")
    public ResponseEntity<Teacher> updateTeacher(
            @PathVariable Long id,
            @RequestBody Teacher updatedTeacher) {

        Teacher teacher = teacherService.updateTeacher(id, updatedTeacher);

        return ResponseEntity.status(HttpStatus.OK)
                .body(teacher);
    }

    @Operation(
            summary = "Delete teacher by ID",
            description = "Deletes the teacher corresponding to the specified teacher ID."
    )

    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Teacher deleted successfully."
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Teacher not found for the specified ID."
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeacher(@PathVariable Long id) {

        teacherService.deleteTeacher(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }
}