package com.aacharya.timetablemanagement.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.aacharya.timetablemanagement.service.SubjectService;
import com.aacharya.timetablemanagement.entity.Subject;
import java.util.List;

@RestController
@RequestMapping("/subjects")
public class SubjectController {
@Autowired
    private  SubjectService subjectService;

@PostMapping
    public Subject saveSubject(@RequestBody Subject subject){
    return subjectService.save(subject);
}

@GetMapping
    public List<Subject> getAllSubject() {
    return subjectService.getAllSubjects();
}
@GetMapping("/{id}")
    public Subject getSubjectById(@PathVariable Long id){
    return subjectService.getSubjectById(id);
}
@PutMapping("/{id}")
    public Subject updateSubject(@PathVariable Long id, @RequestBody Subject updateSubject){
    return subjectService.updateSubject(id,updateSubject);
}
@DeleteMapping("/{id}")
    public  void deleteSubject (@PathVariable Long id){
    subjectService.deleteSubject(id);
}
}
