package com.aacharya.timetablemanagement.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.aacharya.timetablemanagement.service.SubjectService;
import com.aacharya.timetablemanagement.entity.Subject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.List;

@RestController
@RequestMapping("/subjects")
public class SubjectController {
@Autowired
    private  SubjectService subjectService;

@PostMapping
public ResponseEntity<Subject> saveSubject(@RequestBody Subject subject){

    Subject savedSubject = subjectService.save(subject);


    return ResponseEntity.status(HttpStatus.CREATED)
            .body(savedSubject);
}


@GetMapping
    public List<Subject> getAllSubject() {
    return subjectService.getAllSubjects();
}

@GetMapping("/{id}")
    public ResponseEntity<Subject>getSubjectById(@PathVariable Long id){
    Subject subject = subjectService.getSubjectById(id);

    return ResponseEntity.status(HttpStatus.OK)
            .body(subject);
}
    @PutMapping("/{id}")
    public ResponseEntity<Subject> updateSubject(@PathVariable Long id,
                                                 @RequestBody Subject updateSubject){

        Subject subject = subjectService.updateSubject(id, updateSubject);

        return ResponseEntity.status(HttpStatus.OK)
                .body(subject);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubject(@PathVariable Long id){

        subjectService.deleteSubject(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }
}
