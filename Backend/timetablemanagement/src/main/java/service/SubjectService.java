package com.aacharya.timetablemanagement.service;
import com.aacharya.timetablemanagement.entity.Subject;
import com.aacharya.timetablemanagement.entity.Teacher;
import com.aacharya.timetablemanagement.exception.ResourceNotFoundException;
import com.aacharya.timetablemanagement.repository.SubjectRepository;
import com.aacharya.timetablemanagement.repository.TeacherRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class SubjectService {
    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private TeacherRepository teacherRepository;


    public Subject save(Subject subject) {

        Long teacherId = subject.getTeacher().getTeacherId();

        Teacher teacher = teacherRepository.findById(teacherId).orElse(null);

        if (teacher == null) {
            throw new ResourceNotFoundException("Teacher not found with id: " + teacherId);
        }

        subject.setTeacher(teacher);

        return subjectRepository.save(subject);
    }
    public Subject getSubjectById(Long id) {

        Subject subject = subjectRepository.findById(id).orElse(null);

        if (subject == null) {
            throw new ResourceNotFoundException("Subject not found with id: " + id);
        }

        return subject;
    }

    public List<Subject> getAllSubjects() {
        return subjectRepository.findAll();
    }

    public Subject updateSubject(Long subjectId, Subject updateSubject) {

        Subject subject = subjectRepository.findById(subjectId).orElse(null);

        if(subject == null){
            throw new ResourceNotFoundException("Subject not found with id: " + subjectId);
        }
        Long teacherId = updateSubject.getTeacher().getTeacherId();

        Teacher teacher = teacherRepository.findById(teacherId).orElse(null);

            if (teacher == null) {
                throw new ResourceNotFoundException("Teacher not found with id: " + teacherId);
            }

            subject.setSubjectName(updateSubject.getSubjectName());
            subject.setTeacher(teacher);

            return subjectRepository.save(subject);


        //return null;
    }

public void deleteSubject(Long id)
{
         subjectRepository.deleteById(id);

}

}
