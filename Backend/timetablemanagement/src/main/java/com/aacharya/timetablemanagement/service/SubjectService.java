
package com.aacharya.timetablemanagement.service;

import com.aacharya.timetablemanagement.dto.SubjectRequestDTO;
import com.aacharya.timetablemanagement.dto.SubjectResponseDTO;
import com.aacharya.timetablemanagement.entity.Subject;
import com.aacharya.timetablemanagement.entity.Teacher;
import com.aacharya.timetablemanagement.exception.ResourceNotFoundException;
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


    // Helper method to populate teacher and batch details
    private void setTeacherDetails(
            Subject subject,
            SubjectResponseDTO response) {

        if (subject.getTeacher() != null) {

            response.setTeacherName(
                    subject.getTeacher().getName()
            );

            if (subject.getTeacher().getBatch() != null) {

                response.setBatchName(
                        subject.getTeacher().getBatch().getBatchName()
                );
            }
        }
    }

    // Create Subject
    public SubjectResponseDTO save(SubjectRequestDTO requestDTO) {

        Subject subject = new Subject();

        subject.setSubjectName(requestDTO.getSubjectName());
        subject.setSubjectCode(requestDTO.getSubjectCode());
        subject.setCredits(requestDTO.getCredits());

        Long teacherId = requestDTO.getTeacherId();

        Teacher teacher = teacherRepository.findById(teacherId)
                .orElse(null);

        if (teacher == null) {
            throw new ResourceNotFoundException(
                    "Teacher not found with id: " + teacherId
            );
        }

        subject.setTeacher(teacher);

        logger.info("Saving subject: {}", subject.getSubjectName());

        Subject savedSubject = subjectRepository.save(subject);

        logger.info(
                "Subject created successfully: {}",
                savedSubject.getSubjectName()
        );

        SubjectResponseDTO response = new SubjectResponseDTO();

        response.setSubjectName(savedSubject.getSubjectName());
        response.setSubjectCode(savedSubject.getSubjectCode());
        response.setCredits(savedSubject.getCredits());
        response.setSubjectId(savedSubject.getSubjectId());

        setTeacherDetails(savedSubject, response);

        return response;
    }


    // Get Subject By ID
    public SubjectResponseDTO getSubjectById(Long id) {

        logger.info("Fetching subject with id: {}", id);

        Subject subject = subjectRepository.findById(id)
                .orElse(null);

        if (subject == null) {
            throw new ResourceNotFoundException(
                    "Subject not found with id: " + id
            );
        }

        logger.info("Retrieved subject: {}", subject.getSubjectName());

        SubjectResponseDTO response = new SubjectResponseDTO();

        response.setSubjectName(subject.getSubjectName());
        response.setSubjectCode(subject.getSubjectCode());
        response.setCredits(subject.getCredits());
        response.setSubjectId(subject.getSubjectId());

        setTeacherDetails(subject, response);

        return response;
    }


    // Get All Subjects
    public List<SubjectResponseDTO> getAllSubjects() {

        logger.info("Fetching all subjects");

        List<Subject> subjects = subjectRepository.findAll();

        List<SubjectResponseDTO> responseList = new ArrayList<>();

        for (Subject subject : subjects) {

            SubjectResponseDTO response = new SubjectResponseDTO();

            response.setSubjectName(subject.getSubjectName());
            response.setSubjectCode(subject.getSubjectCode());
            response.setCredits(subject.getCredits());
            response.setSubjectId(subject.getSubjectId());

            setTeacherDetails(subject, response);

            responseList.add(response);
        }

        logger.info("Retrieved {} subjects", subjects.size());

        return responseList;
    }


    // ================= Filtering Methods =================


    // Get Subject By Name
    public List<SubjectResponseDTO> getSubjectByName(String subjectName) {

        logger.info("Fetching subjects with name: {}", subjectName);

        List<Subject> subjects =
                subjectRepository.findBySubjectName(subjectName);

        logger.info("Records found: {}", subjects.size());

        if (subjects.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No subject found with name: " + subjectName
            );
        }

        List<SubjectResponseDTO> responseList = new ArrayList<>();

        for (Subject subject : subjects) {

            SubjectResponseDTO response = new SubjectResponseDTO();

            response.setSubjectId(subject.getSubjectId());
            response.setSubjectName(subject.getSubjectName());
            response.setSubjectCode(subject.getSubjectCode());
            response.setCredits(subject.getCredits());

            setTeacherDetails(subject, response);

            responseList.add(response);
        }

        logger.info(
                "Found {} subject(s) with name: {}",
                responseList.size(),
                subjectName
        );

        return responseList;
    }


    // Get Subject By Subject Code
    public List<SubjectResponseDTO> getSubjectBySubjectCode(
            String subjectCode) {

        logger.info("Fetching subjects with code: {}", subjectCode);

        List<Subject> subjects =
                subjectRepository.findBySubjectCode(subjectCode);

        logger.info("Records found: {}", subjects.size());

        if (subjects.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No subject found with code: " + subjectCode
            );
        }

        List<SubjectResponseDTO> responseList = new ArrayList<>();

        for (Subject subject : subjects) {

            SubjectResponseDTO response = new SubjectResponseDTO();

            response.setSubjectId(subject.getSubjectId());
            response.setSubjectName(subject.getSubjectName());
            response.setSubjectCode(subject.getSubjectCode());
            response.setCredits(subject.getCredits());

            setTeacherDetails(subject, response);

            responseList.add(response);
        }

        logger.info(
                "Found {} subject(s) with code: {}",
                responseList.size(),
                subjectCode
        );

        return responseList;
    }


    // ================= Dynamic Filtering =================

    public List<SubjectResponseDTO> getFilteredSubjects(
            String subjectName,
            String subjectCode,
            Integer credits,
            Long teacherId) {

        logger.info(
                "Fetching subjects with filters: name={}, code={}, credits={}, teacherId={}",
                subjectName,
                subjectCode,
                credits,
                teacherId
        );

        List<Subject> subjects = subjectRepository.filterSubjects(
                subjectName,
                subjectCode,
                credits,
                teacherId
        );

        logger.info("Records found: {}", subjects.size());

        if (subjects.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No subject found for the specified filters."
            );
        }

        List<SubjectResponseDTO> responseList = new ArrayList<>();

        for (Subject subject : subjects) {

            SubjectResponseDTO response = new SubjectResponseDTO();

            response.setSubjectId(subject.getSubjectId());
            response.setSubjectName(subject.getSubjectName());
            response.setSubjectCode(subject.getSubjectCode());
            response.setCredits(subject.getCredits());

            setTeacherDetails(subject, response);

            responseList.add(response);
        }

        logger.info(
                "Found {} subject(s) matching the filters.",
                responseList.size()
        );

        return responseList;
    }


    // ================= Update Subject =================

    public SubjectResponseDTO updateSubject(
            Long subjectId,
            SubjectRequestDTO requestDTO) {

        Subject subject = subjectRepository.findById(subjectId)
                .orElse(null);

        if (subject == null) {
            throw new ResourceNotFoundException(
                    "Subject not found with id: " + subjectId
            );
        }

        Long teacherId = requestDTO.getTeacherId();

        Teacher teacher = teacherRepository.findById(teacherId)
                .orElse(null);

        if (teacher == null) {
            throw new ResourceNotFoundException(
                    "Teacher not found with id: " + teacherId
            );
        }

        subject.setSubjectName(requestDTO.getSubjectName());
        subject.setSubjectCode(requestDTO.getSubjectCode());
        subject.setCredits(requestDTO.getCredits());
        subject.setTeacher(teacher);

        logger.info("Updating subject: {}", subject.getSubjectName());

        Subject updatedSubject = subjectRepository.save(subject);

        logger.info(
                "Subject updated successfully: {}",
                updatedSubject.getSubjectName()
        );

        SubjectResponseDTO response = new SubjectResponseDTO();

        response.setSubjectName(updatedSubject.getSubjectName());
        response.setSubjectCode(updatedSubject.getSubjectCode());
        response.setCredits(updatedSubject.getCredits());
        response.setSubjectId(updatedSubject.getSubjectId());

        setTeacherDetails(updatedSubject, response);

        return response;
    }


    // ================= Delete Subject =================

    public void deleteSubject(Long id) {

        Subject subject = subjectRepository.findById(id)
                .orElse(null);

        if (subject == null) {
            throw new ResourceNotFoundException(
                    "Subject not found with id: " + id
            );
        }

        logger.info("Deleting subject: {}", subject.getSubjectName());

        subjectRepository.delete(subject);

        logger.info(
                "Subject deleted successfully: {}",
                subject.getSubjectName()
        );
    }

}
