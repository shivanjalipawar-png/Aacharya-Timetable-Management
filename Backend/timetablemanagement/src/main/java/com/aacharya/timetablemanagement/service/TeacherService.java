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
import com.aacharya.timetablemanagement.dto.TeacherRequestDTO;
import com.aacharya.timetablemanagement.dto.TeacherResponseDTO;
import java.util.ArrayList;


@Service
public class TeacherService {

    @Autowired
    private TeacherRepository teacherRepository;
    private static final Logger logger =
            LoggerFactory.getLogger(TeacherService.class);
    @Autowired
    private BatchRepository batchRepository;


    public TeacherResponseDTO saveTeacher(TeacherRequestDTO requestDTO) {


        Teacher teacher = new Teacher();

        teacher.setName(requestDTO.getTeacherName());
        teacher.setEmail(requestDTO.getEmail());
        teacher.setPhone(requestDTO.getPhone());
        teacher.setQualification(requestDTO.getQualification());
        teacher.setSpecialization(requestDTO.getSpecialization());

        Long batchId = requestDTO.getBatchId();

        Batch batch = batchRepository.findById(batchId).orElse(null);

        if (batch == null) {
            throw new ResourceNotFoundException("Batch not found with id: " + batchId);
        }

        teacher.setBatch(batch);

        logger.info("Saving teacher: {}", teacher.getName());

        Teacher savedTeacher = teacherRepository.save(teacher);

        logger.info("Teacher created successfully: {}", savedTeacher.getName());


        TeacherResponseDTO response = new TeacherResponseDTO();

        response.setTeacherId(savedTeacher.getTeacherId());
        response.setTeacherName(savedTeacher.getName());
        response.setEmail(savedTeacher.getEmail());
        response.setPhone(savedTeacher.getPhone());
        response.setQualification(savedTeacher.getQualification());
        response.setSpecialization(savedTeacher.getSpecialization());
        response.setBatchName(savedTeacher.getBatch().getBatchName());

        return response;
    }

    public List<TeacherResponseDTO> getAllTeachers() {
        logger.info("Fetching all teachers");

        List<Teacher> teachers = teacherRepository.findAll();
        List<TeacherResponseDTO> responseList = new ArrayList<>();
        for (Teacher teacher : teachers) {

            TeacherResponseDTO response = new TeacherResponseDTO();



            responseList.add(response);
        }

        logger.info("Retrieved {} teachers", teachers.size());

        return responseList;
    }

    public TeacherResponseDTO getTeacherById(Long id) {

        logger.info("Fetching teacher with id: {}", id);
        Teacher teacher = teacherRepository.findById(id).orElse(null);

        if (teacher == null) {
            throw new ResourceNotFoundException("Teacher not found with id: " + id);
        }
        logger.info("Retrieved teacher: {}", teacher.getName());
        TeacherResponseDTO response =new TeacherResponseDTO();
      response.setTeacherId(teacher.getTeacherId());
        response.setTeacherName(teacher.getName());
        response.setEmail(teacher.getEmail());
        response.setPhone(teacher.getPhone());
        response.setQualification(teacher.getQualification());
        response.setSpecialization(teacher.getSpecialization());
        response.setBatchName(teacher.getBatch().getBatchName());

        return response;
    }

    public TeacherResponseDTO updateTeacher(
            Long id,
            TeacherRequestDTO requestDTO) {

        Teacher teacher = teacherRepository.findById(id).orElse(null);

        if (teacher == null) {
            throw new ResourceNotFoundException("Teacher not found with id: " + id);
        }
        Long batchId = requestDTO.getBatchId();

        Batch batch = batchRepository.findById(batchId).orElse(null);

        if (batch == null) {
            throw new ResourceNotFoundException("Batch not found with id: " + batchId);
        }



        teacher.setName(requestDTO.getTeacherName());
        teacher.setEmail(requestDTO.getEmail());
        teacher.setPhone(requestDTO.getPhone());
        teacher.setQualification(requestDTO.getQualification());
        teacher.setSpecialization(requestDTO.getSpecialization());
        teacher.setBatch(batch);

        logger.info("Updating teacher: {}", teacher.getTeacherId());

        Teacher savedTeacher = teacherRepository.save(teacher);

        logger.info("Teacher updated successfully: {}", savedTeacher.getTeacherId());
        TeacherResponseDTO response = new TeacherResponseDTO();

//        response.setTeacherId(savedTeacher.getTeacherId());
//        response.setTeacherName(savedTeacher.getName());
//        response.setEmail(savedTeacher.getEmail());
//        response.setPhone(savedTeacher.getPhone());
//        response.setQualification(savedTeacher.getQualification());
//        response.setSpecialization(savedTeacher.getSpecialization());
//        response.setBatchName(savedTeacher.getBatch().getBatchName());

        return response;

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