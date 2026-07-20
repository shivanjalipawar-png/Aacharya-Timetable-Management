package com.aacharya.timetablemanagement.service;

import com.aacharya.timetablemanagement.entity.Teacher;
import com.aacharya.timetablemanagement.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import com.aacharya.timetablemanagement.repository.BatchRepository;
import com.aacharya.timetablemanagement.entity.Batch;

@Service
public class TeacherService {

    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private BatchRepository batchRepository;

    public Teacher saveTeacher(Teacher teacher) {
        Long batchId = teacher.getBatch().getBatchId();

        Batch batch = batchRepository.findById(batchId).orElse(null);
      if(batch==null){
          return null;
      }
        teacher.setBatch(batch);
        return teacherRepository.save(teacher);
    }
    public List<Teacher> getAllTeachers() {
        return teacherRepository.findAll();
    }
    public Teacher getTeacherById(Long id) {
        return teacherRepository.findById(id).orElse(null);
    }
    public Teacher updateTeacher(Long id, Teacher updatedTeacher) {

        Teacher teacher = teacherRepository.findById(id).orElse(null);

        if (teacher != null) {

            teacher.setName(updatedTeacher.getName());
            teacher.setEmail(updatedTeacher.getEmail());
            teacher.setPhone(updatedTeacher.getPhone());
            teacher.setQualification(updatedTeacher.getQualification());
            teacher.setSpecialization(updatedTeacher.getSpecialization());

            return teacherRepository.save(teacher);
        }

        return null;
    }
    public void deleteTeacher(Long id) {
        teacherRepository.deleteById(id);
    }
}