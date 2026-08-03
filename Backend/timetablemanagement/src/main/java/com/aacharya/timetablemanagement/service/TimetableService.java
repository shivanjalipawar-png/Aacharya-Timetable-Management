package com.aacharya.timetablemanagement.service;

import com.aacharya.timetablemanagement.dto.TimetableRequestDTO;
import com.aacharya.timetablemanagement.dto.TimetableResponseDTO;
import com.aacharya.timetablemanagement.entity.Batch;
import com.aacharya.timetablemanagement.entity.Subject;
import com.aacharya.timetablemanagement.entity.Teacher;
import com.aacharya.timetablemanagement.entity.Timetable;
import com.aacharya.timetablemanagement.exception.ConflictException;
import com.aacharya.timetablemanagement.exception.ResourceNotFoundException;
import com.aacharya.timetablemanagement.repository.BatchRepository;
import com.aacharya.timetablemanagement.repository.SubjectRepository;
import com.aacharya.timetablemanagement.repository.TeacherRepository;
import com.aacharya.timetablemanagement.repository.TimetableRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TimetableService {

    @Autowired
    private TimetableRepository timetableRepository;
    private static final Logger logger =
            LoggerFactory.getLogger(TimetableService.class);

    @Autowired
    private TeacherRepository teacherRepository;


    @Autowired
    private BatchRepository batchRepository;

    @Autowired
    private SubjectRepository subjectRepository;




    public TimetableResponseDTO saveTimetable(TimetableRequestDTO requestDTO) {


        Timetable timetable = new Timetable();

        timetable.setClassroom(requestDTO.getClassroom());
        timetable.setStartTime(requestDTO.getStartTime());
        timetable.setEndTime(requestDTO.getEndTime());
        timetable.setDay(requestDTO.getDay());

        Long batchId = requestDTO.getBatchId();

        Batch batch = batchRepository.findById(batchId).orElse(null);

        if (batch == null) {
            throw new ResourceNotFoundException("Batch not found with id: " + batchId);
        }

        timetable.setBatch(batch);

        Long teacherId = requestDTO.getTeacherId();

        Teacher teacher = teacherRepository.findById(teacherId).orElse(null);

        if (teacher == null) {
            throw new ResourceNotFoundException("Teacher not found with id: " + teacherId);
        }

        timetable.setTeacher(teacher);



        Long subjectId = requestDTO.getSubjectId();

        Subject subject = subjectRepository.findById(subjectId).orElse(null);

        if (subject == null) {
            throw new ResourceNotFoundException("Subject not found with id: " + subjectId);
        }

        timetable.setSubject(subject);

        // =====================
       // Teacher Conflict Check
       // =====================
        logger.info("Checking teacher conflict...");
        List<Timetable> teacherConflicts = timetableRepository.findTeacherConflicts(
                teacher.getTeacherId(),
                timetable.getDay(),
                timetable.getStartTime(),
                timetable.getEndTime()
        );

        logger.info("Teacher ID : {}", teacher.getTeacherId());
        logger.info("Day        : {}", timetable.getDay());
        logger.info("Start Time : {}", timetable.getStartTime());
        logger.info("End Time   : {}", timetable.getEndTime());
        logger.info("Teacher conflicts found: {}", teacherConflicts.size());

        if (!teacherConflicts.isEmpty()) {
            throw new ConflictException(
                    "Teacher already has a class during this time."
            );
        }

         // =====================
        // Fetch Batch
        // =====================
        logger.info("Checking Batch  conflict...");
        List<Timetable> batchConflicts = timetableRepository.findBatchConflicts(
                batch.getBatchId(),
                timetable.getDay(),
                timetable.getStartTime(),
                timetable.getEndTime()
        );

        logger.info("Batch conflicts found: {}", batchConflicts.size());
        if (!batchConflicts.isEmpty()) {
            throw new ConflictException(
                    "Batch already has a class during this time."
            );
        }
        // Classroom Conflict
        logger.info("Checking classroom conflict...");
        List<Timetable> classroomConflicts =
                timetableRepository.findClassroomConflicts(
                        timetable.getClassroom(),
                        timetable.getDay(),
                        timetable.getStartTime(),
                        timetable.getEndTime()
                );

        logger.info("Classroom conflicts found: {}", classroomConflicts.size());

        if (!classroomConflicts.isEmpty()) {
            throw new ConflictException(
                    "Classroom already has a scheduled  class during this time."
            );
        }
        // =====================
         // Save Timetable
        // =====================

        logger.info("Saving timetable for batch: {}", batch.getBatchName());

        Timetable savedTimetable = timetableRepository.save(timetable);

        logger.info("Timetable created successfully with id: {}", savedTimetable.getTimetableId());


        TimetableResponseDTO response = new TimetableResponseDTO();

        response.setTimetableId(savedTimetable.getTimetableId());
        response.setDay(savedTimetable.getDay());
        response.setStartTime(savedTimetable.getStartTime());
        response.setEndTime(savedTimetable.getEndTime());
        response.setTeacherName(savedTimetable.getTeacher().getName());
        response.setBatchName(savedTimetable.getBatch().getBatchName());
        response.setSubjectName(savedTimetable.getSubject().getSubjectName());
        response.setClassroom(savedTimetable.getClassroom());
        return response;
    }

    public List<TimetableResponseDTO> getAllTimetables(){
        List<Timetable> timetables = timetableRepository.findAll();
        List<TimetableResponseDTO> timetableResponseDTOArrayList = new ArrayList<>();

        for(Timetable timetable : timetables){
            TimetableResponseDTO response = new TimetableResponseDTO();

            response.setTimetableId(timetable.getTimetableId());
            response.setDay(timetable.getDay());
            response.setStartTime(timetable.getStartTime());
            response.setEndTime(timetable.getEndTime());
            response.setTeacherName(timetable.getTeacher().getName());
            response.setBatchName(timetable.getBatch().getBatchName());
            response.setSubjectName(timetable.getSubject().getSubjectName());
            response.setClassroom(timetable.getClassroom());

            timetableResponseDTOArrayList.add(response);

        }
        return timetableResponseDTOArrayList;

    }


    public List<TimetableResponseDTO> getTimetableByTeacher(Long teacherId){

        logger.info("Fetching timetables for teacher id: {}", teacherId);
        List<Timetable> timetables =
                timetableRepository.findByTeacher_TeacherId(teacherId);
        if (timetables.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No timetable found for teacher id: " + teacherId);
        }
        List<TimetableResponseDTO> responses = new ArrayList<>();

        for (Timetable timetable : timetables) {

            TimetableResponseDTO response = new TimetableResponseDTO();

            response.setTimetableId(timetable.getTimetableId());
            response.setDay(timetable.getDay());
            response.setStartTime(timetable.getStartTime());
            response.setEndTime(timetable.getEndTime());
            response.setTeacherName(timetable.getTeacher().getName());
            response.setBatchName(timetable.getBatch().getBatchName());
            response.setSubjectName(timetable.getSubject().getSubjectName());
            response.setClassroom(timetable.getClassroom());

            responses.add(response);
        }

        logger.info("Found {} timetable(s) for teacher id: {}", responses.size(), teacherId);
        return responses;
    }


    //timetable By batchId
    public List<TimetableResponseDTO> getTimetableByBatch(Long batchId){

        logger.info("Fetching timetables for batch id: {}", batchId);
        List<Timetable> timetables =
                timetableRepository.findByBatch_BatchId(batchId);
        if (timetables.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No timetable found for batch id: " + batchId);
        }
        List<TimetableResponseDTO> responses = new ArrayList<>();

        for (Timetable timetable : timetables) {

            TimetableResponseDTO response = new TimetableResponseDTO();

            response.setTimetableId(timetable.getTimetableId());
            response.setDay(timetable.getDay());
            response.setStartTime(timetable.getStartTime());
            response.setEndTime(timetable.getEndTime());
            response.setTeacherName(timetable.getTeacher().getName());
            response.setBatchName(timetable.getBatch().getBatchName());
            response.setSubjectName(timetable.getSubject().getSubjectName());
            response.setClassroom(timetable.getClassroom());

            responses.add(response);
        }

        logger.info("Found {} timetable(s) for batch id: {}", responses.size(), batchId);
        return responses;
    }

    //timetable By subject
    public List<TimetableResponseDTO> getTimetableBySubject(Long subjectId){

        logger.info("Fetching timetables for subject id: {}", subjectId);
        List<Timetable> timetables =
                timetableRepository.findBySubject_SubjectId(subjectId);
        if (timetables.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No timetable found for subject id: " + subjectId);
        }
        List<TimetableResponseDTO> responses = new ArrayList<>();

        for (Timetable timetable : timetables) {

            TimetableResponseDTO response = new TimetableResponseDTO();

            response.setTimetableId(timetable.getTimetableId());
            response.setDay(timetable.getDay());
            response.setStartTime(timetable.getStartTime());
            response.setEndTime(timetable.getEndTime());
            response.setTeacherName(timetable.getTeacher().getName());
            response.setBatchName(timetable.getBatch().getBatchName());
            response.setSubjectName(timetable.getSubject().getSubjectName());
            response.setClassroom(timetable.getClassroom());

            responses.add(response);
        }

        logger.info("Found {} timetable(s) for subject id: {}", responses.size(), subjectId);
        return responses;
    }




    public TimetableResponseDTO getTimetableById(Long id){
        logger.info("Fetching timetable with id: {}", id);

        Timetable timetable = timetableRepository.findById(id).orElse(null);

        if (timetable == null) {
            throw new ResourceNotFoundException("Timetable not found with id: " + id);
        }
        logger.info("Timetable retrieved  successfully with id: {}", timetable.getTimetableId());

        TimetableResponseDTO response = new TimetableResponseDTO();

        response.setTimetableId(timetable.getTimetableId());
        response.setDay(timetable.getDay());
        response.setStartTime(timetable.getStartTime());
        response.setEndTime(timetable.getEndTime());
        response.setTeacherName(timetable.getTeacher().getName());
        response.setBatchName(timetable.getBatch().getBatchName());
        response.setSubjectName(timetable.getSubject().getSubjectName());
        response.setClassroom(timetable.getClassroom());

        return response;

    }

    public TimetableResponseDTO updateTimetable(Long id, TimetableRequestDTO requestDTO){
        Timetable timetable =
                timetableRepository.findById(id).orElse(null);

        if (timetable == null) {
            throw new ResourceNotFoundException("Timetable not found with id: " + id);
        }

        timetable.setClassroom(requestDTO.getClassroom());
        timetable.setStartTime(requestDTO.getStartTime());
        timetable.setEndTime(requestDTO.getEndTime());
        timetable.setDay(requestDTO.getDay());

        Long teacherId = requestDTO.getTeacherId();

        Teacher teacher = teacherRepository.findById(teacherId).orElse(null);

        if (teacher == null) {
            throw new ResourceNotFoundException("Teacher not found with id: " + teacherId);
        }
        timetable.setTeacher(teacher);

        Long batchId = requestDTO.getBatchId();

        Batch batch = batchRepository.findById(batchId).orElse(null);

        if (batch == null) {
            throw new ResourceNotFoundException("Batch not found with id: " + batchId);
        }
        timetable.setBatch(batch);


        Long subjectId = requestDTO.getSubjectId();

        Subject subject = subjectRepository.findById(subjectId).orElse(null);

        if (subject == null) {
            throw new ResourceNotFoundException("Subject not found with id: " + subjectId);
        }

        timetable.setSubject(subject);
        logger.info("Updating timetable with id: {}", id);
        Timetable savedTimetable = timetableRepository.save(timetable);

        logger.info("Timetable updated  successfully with id: {}", savedTimetable.getTimetableId());


        TimetableResponseDTO response = new TimetableResponseDTO();

        response.setTimetableId(savedTimetable.getTimetableId());
        response.setDay(savedTimetable.getDay());
        response.setStartTime(savedTimetable.getStartTime());
        response.setEndTime(savedTimetable.getEndTime());
        response.setTeacherName(savedTimetable.getTeacher().getName());
        response.setBatchName(savedTimetable.getBatch().getBatchName());
        response.setSubjectName(savedTimetable.getSubject().getSubjectName());
        response.setClassroom(savedTimetable.getClassroom());

        return response;

    }

    public void deleteTimetable(Long id) {

        Timetable timetables = timetableRepository.findById(id).orElse(null);

        if (timetables == null) {
            throw new ResourceNotFoundException("Timetable not found with id: " + id);
        }

        logger.info("Deleting timetable: {}", timetables.getTimetableId());
        timetableRepository.delete(timetables);
        logger.info("Timetable deleted successfully: {}", timetables.getTimetableId());


    }


}
