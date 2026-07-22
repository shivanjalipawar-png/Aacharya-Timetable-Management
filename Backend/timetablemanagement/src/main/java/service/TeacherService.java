package com.aacharya.timetablemanagement.service;

import com.aacharya.timetablemanagement.entity.Teacher;
import com.aacharya.timetablemanagement.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import com.aacharya.timetablemanagement.repository.BatchRepository;
import com.aacharya.timetablemanagement.entity.Batch;
import com.aacharya.timetablemanagement.exception.ResourceNotFoundException;

//import static org.springframework.data.jpa.domain.AbstractPersistable_.id;


@Service
public class TeacherService {

    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private BatchRepository batchRepository;

    public Teacher saveTeacher(Teacher teacher) {

        Long batchId = teacher.getBatch().getBatchId();

        Batch batch = batchRepository.findById(batchId).orElse(null);

        if (batch == null) {
            throw new ResourceNotFoundException("Batch not found with id: " + batchId);
        }

        teacher.setBatch(batch);

        return teacherRepository.save(teacher);
    }

    public List<Teacher> getAllTeachers() {
        return teacherRepository.findAll();
    }

    public Teacher getTeacherById(Long id) {

        Teacher teacher = teacherRepository.findById(id).orElse(null);

        if (teacher == null) {
            throw new ResourceNotFoundException("Teacher not found with id: " + id);
        }

        return teacher;
    }
    public Teacher updateTeacher(Long id, Teacher updatedTeacher) {

        Teacher teacher = teacherRepository.findById(id).orElse(null);

        if (teacher == null) {
            throw new ResourceNotFoundException("Teacher not found with id: " + id);
        }

        teacher.setName(updatedTeacher.getName());
        teacher.setEmail(updatedTeacher.getEmail());
        teacher.setPhone(updatedTeacher.getPhone());
        teacher.setQualification(updatedTeacher.getQualification());
        teacher.setSpecialization(updatedTeacher.getSpecialization());

        return teacherRepository.save(teacher);
    }
    public Batch updateBatch(Long id, Batch updatedBatch) {

        Batch batch = batchRepository.findById(id).orElse(null);

        if (batch == null) {
            throw new ResourceNotFoundException("Batch not found with id: " + id);
        }

        batch.setBatchName(updatedBatch.getBatchName());

        return batchRepository.save(batch);
    }

    public void deleteTeacher(Long id) {

        Teacher teacher = teacherRepository.findById(id).orElse(null);

        if (teacher == null) {
            throw new ResourceNotFoundException("Teacher not found with id: " + id);
        }

        teacherRepository.delete(teacher);
    }
}