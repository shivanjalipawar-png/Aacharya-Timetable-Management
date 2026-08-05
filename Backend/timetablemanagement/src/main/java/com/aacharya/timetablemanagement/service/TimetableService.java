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
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
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
                // timetable.getTimetableId()
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
                //timetable.getTimetableId()
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
                        //timetable.getTimetableId()
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
    //New method for Day
    public List<TimetableResponseDTO> getTimetableByDay(DayOfWeek day) {
        logger.info("Fetching timetable for Day:{}",day);
        List<Timetable> timetables= timetableRepository.findByDay(day);

        //throw exception
        if(timetables.isEmpty()){
            throw new ResourceNotFoundException("timetable not foud for this day:"+day);
        }

        //Entity -> DTO
        List<TimetableResponseDTO>  response =new ArrayList<>();
        for(Timetable timetable: timetables){
             TimetableResponseDTO dto = new TimetableResponseDTO();

             dto.setTimetableId(timetable.getTimetableId());
             dto.setDay(timetable.getDay());
             dto.setStartTime(timetable.getStartTime());
             dto.setEndTime(timetable.getEndTime());
             dto.setBatchName(timetable.getBatch().getBatchName());
             dto.setSubjectName(timetable.getSubject().getSubjectName());
             dto.setTeacherName(timetable.getTeacher().getName());
             dto.setClassroom(timetable.getClassroom());

         response.add(dto);
        }

        logger.info("Found {} timetable for the day{}", response.size(), day);
       return response;
    }

    // Get timetable by classroom
    public List<TimetableResponseDTO> getTimetableByClassroom(String classroom) {
        logger.info("Fetching timetable for classroom:{}",classroom);
        List<Timetable> timetables= timetableRepository.findByClassroom(classroom);
        logger.info("Records found: {}", timetables.size());

        //throw exception
        if(timetables.isEmpty()){
            throw new ResourceNotFoundException("timetable not foud for this classroom:"+classroom);
        }

        //Entity -> DTO
        List<TimetableResponseDTO>  response =new ArrayList<>();
        for(Timetable timetable: timetables){
            TimetableResponseDTO dto = new TimetableResponseDTO();

            dto .setTimetableId(timetable.getTimetableId());
            dto.setDay(timetable.getDay());
            dto.setStartTime(timetable.getStartTime());
            dto.setEndTime(timetable.getEndTime());
            dto.setBatchName(timetable.getBatch().getBatchName());
            dto.setSubjectName(timetable.getSubject().getSubjectName());
            dto.setTeacherName(timetable.getTeacher().getName());
            dto.setClassroom(timetable.getClassroom());

            response.add(dto);
        }

        logger.info("Found {} timetable for the classroom{}", response.size(), classroom);
        return response;
    }

//Get timetable by Time range
    public List<TimetableResponseDTO> getTimetableByTimeRange( LocalTime startTime,  LocalTime endTime ){

        logger.info("Fetching timetables for time rage: {}-{}",startTime,endTime);

        List<Timetable> timetables =
                timetableRepository.findTimetableByTimeRange(startTime,endTime);
        if (timetables.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No timetable found for time range : " + startTime+"-"+endTime);
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

        logger.info("Found {} timetable(s) for time range: {}-{}", responses.size(), startTime,endTime);
        return responses;
    }

// fetching timetable for today=day
public List<TimetableResponseDTO> getTodayTimetable() {


    DayOfWeek today = LocalDate.now().getDayOfWeek();

    logger.info("Fetching timetable for today: {}", today);

    // Fetch today's timetables
    List<Timetable> timetables = timetableRepository.findByDay(today);
    if (timetables.isEmpty()) {
        throw new ResourceNotFoundException(
                "No timetable found for today: " + today
        );
    }


    //Entity -> DTO
    List<TimetableResponseDTO>  response =new ArrayList<>();
    for(Timetable timetable: timetables){
        TimetableResponseDTO dto = new TimetableResponseDTO();

        dto.setTimetableId(timetable.getTimetableId());
        dto.setDay(timetable.getDay());
        dto.setStartTime(timetable.getStartTime());
        dto.setEndTime(timetable.getEndTime());
        dto.setBatchName(timetable.getBatch().getBatchName());
        dto.setSubjectName(timetable.getSubject().getSubjectName());
        dto.setTeacherName(timetable.getTeacher().getName());
        dto.setClassroom(timetable.getClassroom());

        response.add(dto);
    }

    logger.info("Found {} timetable for today: {}", response.size(), today);
    return response;
}

//get timetable for Teacher + day
public List<TimetableResponseDTO> getTimetableByTeacherAndDay(
        Long teacherId,
        DayOfWeek day){
    logger.info("Fetching timetables by teacher and day: {}-{}",teacherId,day);

    List<Timetable> timetables =
            timetableRepository.findByTeacher_TeacherIdAndDay(
                    teacherId,
                    day
            );
    if (timetables.isEmpty()) {
        throw new ResourceNotFoundException(
                "No timetable found for teacherId & day: " + teacherId+"-"+day
        );
    }

    //Entity -> DTO
    List<TimetableResponseDTO>  response =new ArrayList<>();
    for(Timetable timetable: timetables){
        TimetableResponseDTO dto = new TimetableResponseDTO();

        dto.setTimetableId(timetable.getTimetableId());
        dto.setDay(timetable.getDay());
        dto.setStartTime(timetable.getStartTime());
        dto.setEndTime(timetable.getEndTime());
        dto.setBatchName(timetable.getBatch().getBatchName());
        dto.setSubjectName(timetable.getSubject().getSubjectName());
        dto.setTeacherName(timetable.getTeacher().getName());
        dto.setClassroom(timetable.getClassroom());

        response.add(dto);
    }

    logger.info("Found {} timetable for today: {}-{}", response.size(), teacherId,day);
    return response;
}

    //get timetable for Teacher + day
    public List<TimetableResponseDTO> getTimetableByBatchAndDay(
            Long batchId,
            DayOfWeek day){
        logger.info("Fetching timetables by batch and day: {}-{}",batchId,day);

        List<Timetable> timetables =
                timetableRepository.findByBatch_BatchIdAndDay(
                        batchId,
                        day
                );
        if (timetables.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No timetable found for batchId & day: " + batchId+"-"+day
            );
        }

        //Entity -> DTO
        List<TimetableResponseDTO>  response =new ArrayList<>();
        for(Timetable timetable: timetables){
            TimetableResponseDTO dto = new TimetableResponseDTO();

            dto.setTimetableId(timetable.getTimetableId());
            dto.setDay(timetable.getDay());
            dto.setStartTime(timetable.getStartTime());
            dto.setEndTime(timetable.getEndTime());
            dto.setBatchName(timetable.getBatch().getBatchName());
            dto.setSubjectName(timetable.getSubject().getSubjectName());
            dto.setTeacherName(timetable.getTeacher().getName());
            dto.setClassroom(timetable.getClassroom());

            response.add(dto);
        }

        logger.info("Found {} timetable for today: {}-{}", response.size(), batchId,day);
        return response;
    }


    public TimetableResponseDTO updateTimetable(Long id, TimetableRequestDTO requestDTO){
        Timetable timetable = timetableRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Timetable not found with id: " + id));

        timetable.setClassroom(requestDTO.getClassroom());
        timetable.setStartTime(requestDTO.getStartTime());
        timetable.setEndTime(requestDTO.getEndTime());
        timetable.setDay(requestDTO.getDay());

        Long teacherId = requestDTO.getTeacherId();

        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Teacher not found with id: " + teacherId));

        timetable.setTeacher(teacher);
        // Teacher Conflict Check
        // =====================
        logger.info("Checking teacher conflict...");
        List<Timetable> teacherConflicts = timetableRepository.updateTeacherConflicts(
                teacher.getTeacherId(),
                timetable.getDay(),
                timetable.getStartTime(),
                timetable.getEndTime(),
                timetable.getTimetableId()
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

        // =====================
        // Fetch Batch
        // =====================
        logger.info("Checking batch conflict...");
        List<Timetable> batchConflicts = timetableRepository.updateBatchConflicts(
                batch.getBatchId(),
                timetable.getDay(),
                timetable.getStartTime(),
                timetable.getEndTime(),
                timetable.getTimetableId()
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
                timetableRepository.updateClassroomConflicts(
                        timetable.getClassroom(),
                        timetable.getDay(),
                        timetable.getStartTime(),
                        timetable.getEndTime(),
                        timetable.getTimetableId()
                );

        logger.info("Classroom conflicts found: {}", classroomConflicts.size());

        if (!classroomConflicts.isEmpty()) {
            throw new ConflictException(
                    "Classroom already has a scheduled  class during this time."
            );
        }

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

        logger.info("Delete request received for id: {}", id);

        Timetable timetable = timetableRepository.findById(id).orElse(null);

        logger.info("Fetched timetable object: {}", timetable);

        if (timetable == null) {
            throw new ResourceNotFoundException(
                    "Timetable not found with id: " + id);
        }

        timetableRepository.delete(timetable);

        logger.info("Deleted successfully.");
        logger.info("Timetable deleted successfully with id: {}", id);
    }


}
