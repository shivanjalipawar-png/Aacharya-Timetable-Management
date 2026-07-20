package com.aacharya.timetablemanagement.controller;

import com.aacharya.timetablemanagement.entity.Teacher;
import com.aacharya.timetablemanagement.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/teachers")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    @PostMapping
    public Teacher saveTeacher(@RequestBody Teacher teacher) {
        return teacherService.saveTeacher(teacher);
    }
    @GetMapping
    public List<Teacher> getAllTeachers() {
        return teacherService.getAllTeachers();
    }
    @GetMapping("/{id}")
    public Teacher getTeacherById(@PathVariable Long id) {
        return teacherService.getTeacherById(id);
    }
    @PutMapping("/{id}")
    public Teacher updateTeacher(@PathVariable Long id,
                                 @RequestBody Teacher updatedTeacher) {

        return teacherService.updateTeacher(id, updatedTeacher);

    }
    @DeleteMapping("/{id}")
    public void deleteTeacher(@PathVariable Long id) {
        teacherService.deleteTeacher(id);
    }
}