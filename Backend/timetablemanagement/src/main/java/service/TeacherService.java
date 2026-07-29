package com.aacharya.timetablemanagement.service;

import com.aacharya.timetablemanagement.entity.Teacher;
import com.aacharya.timetablemanagement.repository.TeacherRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import com.aacharya.timetablemanagement.repository.BatchRepository;
import com.aacharya.timetablemanagement.entity.Batch;
import com.aacharya.timetablemanagement.exception.ResourceNotFoundException;



@Service
public class TeacherService {

    @Autowired
    private TeacherRepository teacherRepository;
    private static final Logger logger =
            LoggerFactory.getLogger(TeacherService.class);
    @Autowired
    private BatchRepository batchRepository;



    public Teacher saveTeacher(Teacher teacher) {

        Long batchId = teacher.getBatch().getBatchId();

        Batch batch = batchRepository.findById(batchId).orElse(null);

        if (batch == null) {
            throw new ResourceNotFoundException("Batch not found with id: " + batchId);
        }

        teacher.setBatch(batch);

        logger.info("Saving teacher: {}", teacher.getTeacherId());

        Teacher savedTeacher = teacherRepository.save(teacher);

        logger.info("Saving teacher: {}", teacher.getName());

        return savedTeacher;
    }

    public List<Teacher> getAllTeachers() {
        logger.info("Fetching all teachers");

        List<Teacher> teachers = teacherRepository.findAll();

        logger.info("Retrieved {} teachers", teachers.size());

        return teachers;
    }

    public Teacher getTeacherById(Long id) {

        logger.info("Fetching teacher with id: {}", id);
        Teacher teacher = teacherRepository.findById(id).orElse(null);

        if (teacher == null) {
            throw new ResourceNotFoundException("Teacher not found with id: " + id);
        }
        logger.info("Retrieved teacher: {}", teacher.getName());

        return teacher;
    }
    public Teacher updateTeacher(Long id, Teacher updatedTeacher) {

        Teacher teacher = teacherRepository.findById(id).orElse(null);

        if (teacher == null) {
            throw new ResourceNotFoundException("Teacher not found with id: " + id);
        }
        Long batchId = updatedTeacher.getBatch().getBatchId();

        Batch batch = batchRepository.findById(batchId).orElse(null);

        if (batch == null) {
            throw new ResourceNotFoundException("Batch not found with id: " + batchId);
        }

        teacher.setBatch(batch);

        teacher.setName(updatedTeacher.getName());
        teacher.setEmail(updatedTeacher.getEmail());
        teacher.setPhone(updatedTeacher.getPhone());
        teacher.setQualification(updatedTeacher.getQualification());
        teacher.setSpecialization(updatedTeacher.getSpecialization());

        logger.info("Updating teacher: {}", teacher.getTeacherId());

        Teacher savedTeacher = teacherRepository.save(teacher);

        logger.info("Teacher updated successfully: {}", savedTeacher.getTeacherId());

        return savedTeacher;

    }


    public void deleteTeacher(Long id) {

        Teacher teacher = teacherRepository.findById(id).orElse(null);

        if (teacher == null) {
            throw new ResourceNotFoundException("Teacher not found with id: " + id);
        }

        logger.info("Deleting teacher: {}", teacher.getTeacherId());
        teacherRepository.delete(teacher);
        logger.info("Teacher deleted successfully: {}", teacher.getName());


    }
}