package com.aacharya.timetablemanagement.service;
import com.aacharya.timetablemanagement.dto.SubjectRequestDTO;
import com.aacharya.timetablemanagement.dto.SubjectResponseDTO;
import com.aacharya.timetablemanagement.entity.Subject;
import com.aacharya.timetablemanagement.entity.Teacher;
import com.aacharya.timetablemanagement.exception.ResourceNotFoundException;
import com.aacharya.timetablemanagement.repository.BatchRepository;
import com.aacharya.timetablemanagement.repository.SubjectRepository;
import com.aacharya.timetablemanagement.repository.TeacherRepository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



@Service
public class SubjectService {
    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private TeacherRepository teacherRepository;
    private static final Logger logger =
            LoggerFactory.getLogger(SubjectService.class);
    @Autowired
    private BatchRepository batchRepository;


    public SubjectResponseDTO save(SubjectRequestDTO requestDTO) {

         Subject subject = new Subject();

         subject.setSubjectName(requestDTO.getSubjectName());
        subject.setSubjectCode(requestDTO.getSubjectCode());
        subject.setCredits(requestDTO.getCredits());

        //Batch batch = batchRepository.findById(requestDTO.getTeacherId()).orElse(null);
        Long teacherId = requestDTO.getTeacherId();

        Teacher teacher = teacherRepository.findById(teacherId).orElse(null);

        if (teacher == null) {
            throw new ResourceNotFoundException("Teacher not found with id: " + teacherId);
        }

        subject.setTeacher(teacher);

        logger.info("Saving subject: {}", subject.getSubjectName());

        Subject savedSubject = subjectRepository.save(subject);

        logger.info("Subject created successfully: {}", savedSubject.getSubjectName());

        SubjectResponseDTO response= new SubjectResponseDTO();

        response.setSubjectName(savedSubject.getSubjectName());
        response.setSubjectCode(savedSubject.getSubjectCode());
        response.setCredits(savedSubject.getCredits());
        response.setSubjectId(savedSubject.getSubjectId());
      //  response.setBatchName(savedSubject.getBatchName());

        if(subject.getTeacher().getBatch() != null){
            response.setBatchName(subject.getTeacher().getBatch().getBatchName());
        }
         return response;
    }




    public SubjectResponseDTO getSubjectById(Long id) {
        logger.info("Fetching subject with id: {}", id);
        Subject subject = subjectRepository.findById(id).orElse(null);

        if (subject == null) {
            throw new ResourceNotFoundException("Subject not found with id: " + id);
        }

        logger.info("Retrieved subject: {}", subject.getSubjectName());

        SubjectResponseDTO response= new SubjectResponseDTO();


        response.setSubjectName(subject.getSubjectName());
        response.setSubjectCode(subject.getSubjectCode());
        response.setCredits(subject.getCredits());
        response.setSubjectId(subject.getSubjectId());
      //  response.setBatchName(subject.getBatchName());

        if(subject.getTeacher().getBatch() != null){
            response.setBatchName(subject.getTeacher().getBatch().getBatchName());
        }
        return response;
    }



    public List<SubjectResponseDTO> getAllSubjects() {

        logger.info("Fetching all subjects");

        List<Subject> subjects = subjectRepository.findAll();
        List<SubjectResponseDTO> responseList= new ArrayList<>();
        for(Subject subject:subjects){
            SubjectResponseDTO response= new SubjectResponseDTO();
            response.setSubjectName(subject.getSubjectName());
            response.setSubjectCode(subject.getSubjectCode());
            response.setCredits(subject.getCredits());
            response.setSubjectId(subject.getSubjectId());
           // response.setBatchName(subject.getBatchName());
            if(subject.getTeacher().getBatch() != null){
                response.setBatchName(subject.getTeacher().getBatch().getBatchName());
            }
         responseList.add(response);
        }
        logger.info("Retrieved {} subjects", subjects.size());

        return responseList;

    }

    public SubjectResponseDTO updateSubject(Long subjectId, SubjectRequestDTO requestDTO) {

        Subject subject = subjectRepository.findById(subjectId).orElse(null);

        if(subject == null){
            throw new ResourceNotFoundException("Subject not found with id: " + subjectId);
        }
        //Batch batch = batchRepository.findById(requestDTO.getBatchId(batchId)).orElse(null);
        Long teacherId = requestDTO.getTeacherId();

        Teacher teacher = teacherRepository.findById(teacherId).orElse(null);

            if (teacher == null) {
                throw new ResourceNotFoundException("Teacher not found with id: " + teacherId);
            }


        subject.setSubjectName(requestDTO.getSubjectName());
        subject.setSubjectCode(requestDTO.getSubjectCode());
        subject.setCredits(requestDTO.getCredits());
        logger.info("Updating subject: {}", subject.getSubjectName());

        Subject updatedSubject = subjectRepository.save(subject);

        logger.info("Subject updated successfully: {}", updatedSubject.getSubjectName());

          SubjectResponseDTO response = new SubjectResponseDTO();


        response.setSubjectName(subject.getSubjectName());
        response.setSubjectCode(subject.getSubjectCode());
        response.setCredits(subject.getCredits());
        response.setSubjectId(subject.getSubjectId());
      //  response.setBatchName(subject.getBatchName());
   subject.setTeacher(teacher);
        return response;


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
