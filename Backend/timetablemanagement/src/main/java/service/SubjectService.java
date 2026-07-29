package com.aacharya.timetablemanagement.service;
import com.aacharya.timetablemanagement.entity.Subject;
import com.aacharya.timetablemanagement.entity.Teacher;
import com.aacharya.timetablemanagement.exception.ResourceNotFoundException;
import com.aacharya.timetablemanagement.repository.SubjectRepository;
import com.aacharya.timetablemanagement.repository.TeacherRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



@Service
public class SubjectService {
    @Autowired
    private SubjectRepository subjectRepository;
    private static final Logger logger =
            LoggerFactory.getLogger(SubjectService.class);
    @Autowired
    private TeacherRepository teacherRepository;


    public Subject save(Subject subject) {

        Long teacherId = subject.getTeacher().getTeacherId();

        Teacher teacher = teacherRepository.findById(teacherId).orElse(null);

        if (teacher == null) {
            throw new ResourceNotFoundException("Teacher not found with id: " + teacherId);
        }

        subject.setTeacher(teacher);

        logger.info("Saving subject: {}", subject.getSubjectName());

        Subject savedSubject = subjectRepository.save(subject);

        logger.info("Subject created successfully: {}", savedSubject.getSubjectName());

        return savedSubject;
    }
    public Subject getSubjectById(Long id) {
        logger.info("Fetching subject with id: {}", id);
        Subject subject = subjectRepository.findById(id).orElse(null);

        if (subject == null) {
            throw new ResourceNotFoundException("Subject not found with id: " + id);
        }

        logger.info("Retrieved subject: {}", subject.getSubjectName());

        return subject;
    }

    public List<Subject> getAllSubjects() {

        logger.info("Fetching all subjects");

        List<Subject> subjects = subjectRepository.findAll();

        logger.info("Retrieved {} subjects", subjects.size());

        return subjects;
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
        logger.info("Updating subject: {}", subject.getSubjectName());


        Subject updatedSubject = subjectRepository.save(subject);

        logger.info("Subject updated successfully: {}", updatedSubject.getSubjectName());


        return updatedSubject;


           //return subjectRepository.findById(Long id);



    }

public void deleteSubject(Long id)
{
    Subject subject = subjectRepository.findById(id).orElse(null);
    if(subject == null){
        throw new ResourceNotFoundException("Subject not found with id: " + id);
    }
    logger.info("Deleting subject: {}", subject.getSubjectName());
    subjectRepository.delete(subject);
    logger.info("Subject deleted successfully: {}", subject.getSubjectName());


}

}
