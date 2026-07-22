package com.aacharya.timetablemanagement.controller;

import com.aacharya.timetablemanagement.entity.Teacher;
import com.aacharya.timetablemanagement.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/teachers")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;


    @PostMapping
    public ResponseEntity<Teacher> saveTeacher(@RequestBody Teacher teacher) {

        Teacher savedTeacher = teacherService.saveTeacher(teacher);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(savedTeacher);
    }


    @GetMapping
    public ResponseEntity<List<Teacher>> getAllTeachers() {

        return ResponseEntity.status(HttpStatus.OK)
                .body(teacherService.getAllTeachers());
    }


    @GetMapping("/{id}")
    public ResponseEntity<Teacher> getTeacherById(@PathVariable Long id) {

        Teacher teacher = teacherService.getTeacherById(id);

        return ResponseEntity.status(HttpStatus.OK)
                .body(teacher);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Teacher> updateTeacher(
            @PathVariable Long id,
            @RequestBody Teacher updatedTeacher) {

        Teacher teacher = teacherService.updateTeacher(id, updatedTeacher);

        return ResponseEntity.status(HttpStatus.OK)
                .body(teacher);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeacher(@PathVariable Long id) {

        teacherService.deleteTeacher(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }
}