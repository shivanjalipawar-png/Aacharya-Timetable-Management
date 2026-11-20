
package com.aacharya.timetablemanagement.service;

import com.aacharya.timetablemanagement.dto.TimetableRequestDTO;
import com.aacharya.timetablemanagement.dto.TimetableResponseDTO;
import com.aacharya.timetablemanagement.entity.Batch;
import com.aacharya.timetablemanagement.entity.Subject;
import com.aacharya.timetablemanagement.entity.Teacher;
import com.aacharya.timetablemanagement.entity.Timetable;
import com.aacharya.timetablemanagement.entity.User;
import com.aacharya.timetablemanagement.entity.Role;
import com.aacharya.timetablemanagement.exception.ConflictException;
import com.aacharya.timetablemanagement.exception.ResourceNotFoundException;
import com.aacharya.timetablemanagement.repository.BatchRepository;
import com.aacharya.timetablemanagement.repository.SubjectRepository;
import com.aacharya.timetablemanagement.repository.TeacherRepository;
import com.aacharya.timetablemanagement.repository.TimetableRepository;
import com.aacharya.timetablemanagement.repository.UserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    @Autowired
    private UserRepository userRepository;


    // =========================
    // CREATE TIMETABLE
    // =========================

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
        // Batch Conflict Check
        // =====================

        logger.info("Checking Batch conflict...");

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


        // =====================
        // Classroom Conflict Check
        // =====================

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
                    "Classroom already has a scheduled class during this time."
            );
        }


        // =====================
        // Save Timetable
        // =====================

        logger.info("Saving timetable for batch: {}", batch.getBatchName());

        Timetable savedTimetable = timetableRepository.save(timetable);

        logger.info("Timetable created successfully with id: {}",
                savedTimetable.getTimetableId());

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


    // =========================
    // GET ALL TIMETABLES
    // =========================

    public Page<TimetableResponseDTO> getAllTimetables(Pageable pageable) {

        logger.info("Fetching timetables - Page: {}, Size: {}",
                pageable.getPageNumber(),
                pageable.getPageSize());

        User currentUser = getCurrentUser();

        Page<Timetable> timetables;

        if (currentUser.getRole() == Role.ADMIN) {

            timetables = timetableRepository.findAll(pageable);

        } else if (currentUser.getRole() == Role.TEACHER) {

            Long teacherId = getCurrentTeacherId();

            timetables = timetableRepository.findByTeacher_TeacherId(
                    teacherId, pageable);

        } else if (currentUser.getRole() == Role.STUDENT) {

            Long batchId = getCurrentBatchId();

            timetables = timetableRepository.findByBatch_BatchId(
                    batchId, pageable);

        } else {
            throw new AccessDeniedException("Access denied");
        }

        return timetables.map(timetable -> {

            TimetableResponseDTO dto = new TimetableResponseDTO();

            dto.setTimetableId(timetable.getTimetableId());
            dto.setDay(timetable.getDay());
            dto.setStartTime(timetable.getStartTime());
            dto.setEndTime(timetable.getEndTime());
            dto.setTeacherName(timetable.getTeacher().getName());
            dto.setBatchName(timetable.getBatch().getBatchName());
            dto.setSubjectName(timetable.getSubject().getSubjectName());
            dto.setClassroom(timetable.getClassroom());

            return dto;
        });
    }


    // =========================
    // GET TIMETABLE BY TEACHER
    // =========================

    public List<TimetableResponseDTO> getTimetableByTeacher(Long teacherId) {

        logger.info("Fetching timetables for teacher id: {}", teacherId);

        List<Timetable> timetables = getAccessibleTimetables().stream()
                .filter(t -> t.getTeacher().getTeacherId().equals(teacherId))
                .toList();

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

        logger.info("Found {} timetable(s) for teacher id: {}",
                responses.size(), teacherId);

        return responses;
    }


    // =========================
    // DYNAMIC FILTERING
    // =========================


    public List<TimetableResponseDTO> getFilteredTimetables(
            Long teacherId,
            Long batchId,
            DayOfWeek day,
            Long subjectId,
            String classroom) {

        logger.info(
                "Filtering timetables for teacherId, batchId, day, subjectId and classroom: {}-{}-{}-{}-{}",
                teacherId, batchId, day, subjectId, classroom);

        // First restrict records to the logged-in user's access.
        // Then apply the optional filters supplied in the request.

        List<Timetable> timetables = getAccessibleTimetables()
                .stream()
                .filter(t -> teacherId == null ||
                        Objects.equals(t.getTeacher().getTeacherId(), teacherId))
                .filter(t -> batchId == null ||
                        Objects.equals(t.getBatch().getBatchId(), batchId))
                .filter(t -> day == null ||
                        Objects.equals(t.getDay(), day))
                .filter(t -> subjectId == null ||
                        Objects.equals(t.getSubject().getSubjectId(), subjectId))
                .filter(t -> classroom == null ||
                        classroom.equals(t.getClassroom()))
                .toList();

        if (timetables.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No timetable found for teacherId=" + teacherId +
                            ", batchId=" + batchId +
                            ", day=" + day +
                            ", subjectId=" + subjectId +
                            ", classroom=" + classroom
            );
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

        logger.info("Found {} filtered timetable(s)", responses.size());

        return responses;
    }



    // =========================
    // GET TIMETABLE BY BATCH
    // =========================

    public List<TimetableResponseDTO> getTimetableByBatch(Long batchId) {

        logger.info("Fetching timetables for batch id: {}", batchId);

        List<Timetable> timetables = getAccessibleTimetables().stream()
                .filter(t -> t.getBatch().getBatchId().equals(batchId))
                .toList();

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

        logger.info("Found {} timetable(s) for batch id: {}",
                responses.size(), batchId);

        return responses;
    }


    // =========================
    // GET TIMETABLE BY SUBJECT
    // =========================

    public List<TimetableResponseDTO> getTimetableBySubject(Long subjectId) {

        logger.info("Fetching timetables for subject id: {}", subjectId);

        List<Timetable> timetables = getAccessibleTimetables().stream()
                .filter(t -> t.getSubject().getSubjectId()==subjectId)
                .toList();

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

        logger.info("Found {} timetable(s) for subject id: {}",
                responses.size(), subjectId);

        return responses;
    }


    // =========================
    // GET TIMETABLE BY ID
    // =========================

    public TimetableResponseDTO getTimetableById(Long id) {

        logger.info("Fetching timetable with id: {}", id);

        Timetable timetable = getAccessibleTimetables().stream()
                .filter(t -> t.getTimetableId().equals(id))
                .findFirst()
                .orElse(null);

        if (timetable == null) {
            throw new ResourceNotFoundException(
                    "Timetable not found with id: " + id);
        }

        logger.info("Timetable retrieved successfully with id: {}",
                timetable.getTimetableId());

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


    // =========================
    // GET TIMETABLE BY DAY
    // =========================

    public List<TimetableResponseDTO> getTimetableByDay(DayOfWeek day) {

        logger.info("Fetching timetable for Day: {}", day);

        List<Timetable> timetables = getAccessibleTimetables().stream()
                .filter(t -> t.getDay() == day)
                .toList();

        if (timetables.isEmpty()) {
            throw new ResourceNotFoundException(
                    "Timetable not found for this day: " + day);
        }

        List<TimetableResponseDTO> response = new ArrayList<>();

        for (Timetable timetable : timetables) {

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

        logger.info("Found {} timetable(s) for the day {}",
                response.size(), day);

        return response;
    }


    // =========================
    // GET TIMETABLE BY CLASSROOM
    // =========================

    public List<TimetableResponseDTO> getTimetableByClassroom(String classroom) {

        logger.info("Fetching timetable for classroom: {}", classroom);

        List<Timetable> timetables = getAccessibleTimetables().stream()
                .filter(t -> classroom.equals(t.getClassroom()))
                .toList();

        logger.info("Records found: {}", timetables.size());

        if (timetables.isEmpty()) {
            throw new ResourceNotFoundException(
                    "Timetable not found for this classroom: " + classroom);
        }

        List<TimetableResponseDTO> response = new ArrayList<>();

        for (Timetable timetable : timetables) {

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

        logger.info("Found {} timetable(s) for the classroom {}",
                response.size(), classroom);

        return response;
    }


    // =========================
    // GET TIMETABLE BY TIME RANGE
    // =========================

    public List<TimetableResponseDTO> getTimetableByTimeRange(
            LocalTime startTime,
            LocalTime endTime) {

        logger.info("Fetching timetables for time range: {}-{}",
                startTime, endTime);

        List<Timetable> timetables = getAccessibleTimetables().stream()
                .filter(t -> startTime.isBefore(t.getEndTime()) &&
                        endTime.isAfter(t.getStartTime()))
                .toList();

        if (timetables.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No timetable found for time range: " +
                            startTime + "-" + endTime);
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

        logger.info("Found {} timetable(s) for time range: {}-{}",
                responses.size(), startTime, endTime);

        return responses;
    }


    // =========================
    // GET TODAY'S TIMETABLE
    // =========================

    public List<TimetableResponseDTO> getTodayTimetable() {

        DayOfWeek today = LocalDate.now().getDayOfWeek();

        logger.info("Fetching timetable for today: {}", today);

        List<Timetable> timetables = getAccessibleTimetables().stream()
                .filter(t -> t.getDay() == today)
                .toList();

        if (timetables.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No timetable found for today: " + today);
        }

        List<TimetableResponseDTO> response = new ArrayList<>();

        for (Timetable timetable : timetables) {

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

        logger.info("Found {} timetable(s) for today: {}",
                response.size(), today);

        return response;
    }


    // =========================
    // GET TIMETABLE BY TEACHER AND DAY
    // =========================

    public List<TimetableResponseDTO> getTimetableByTeacherAndDay(
            Long teacherId,
            DayOfWeek day) {

        logger.info("Fetching timetables by teacher and day: {}-{}",
                teacherId, day);

        List<Timetable> timetables = getAccessibleTimetables().stream()
                .filter(t -> t.getTeacher().getTeacherId().equals(teacherId)
                        && t.getDay() == day)
                .toList();

        if (timetables.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No timetable found for teacherId & day: " +
                            teacherId + "-" + day);
        }

        List<TimetableResponseDTO> response = new ArrayList<>();

        for (Timetable timetable : timetables) {

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

        logger.info("Found {} timetable(s) for teacher and day: {}-{}",
                response.size(), teacherId, day);

        return response;
    }


    // =========================
    // GET TIMETABLE BY BATCH AND DAY
    // =========================

    public List<TimetableResponseDTO> getTimetableByBatchAndDay(
            Long batchId,
            DayOfWeek day) {

        logger.info("Fetching timetables by batch and day: {}-{}",
                batchId, day);

        List<Timetable> timetables = getAccessibleTimetables().stream()
                .filter(t -> t.getBatch().getBatchId().equals(batchId)
                        && t.getDay() == day)
                .toList();

        if (timetables.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No timetable found for batchId & day: " +
                            batchId + "-" + day);
        }

        List<TimetableResponseDTO> response = new ArrayList<>();

        for (Timetable timetable : timetables) {

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

        logger.info("Found {} timetable(s) for batch and day: {}-{}",
                response.size(), batchId, day);

        return response;
    }


    // =========================
    // UPDATE TIMETABLE
    // =========================

    public TimetableResponseDTO updateTimetable(
            Long id,
            TimetableRequestDTO requestDTO) {

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

        logger.info("Checking teacher conflict...");

        List<Timetable> teacherConflicts =
                timetableRepository.updateTeacherConflicts(
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
            throw new ResourceNotFoundException(
                    "Batch not found with id: " + batchId);
        }

        timetable.setBatch(batch);


        Long subjectId = requestDTO.getSubjectId();

        Subject subject = subjectRepository.findById(subjectId).orElse(null);

        if (subject == null) {
            throw new ResourceNotFoundException(
                    "Subject not found with id: " + subjectId);
        }

        timetable.setSubject(subject);


        // Batch Conflict Check

        logger.info("Checking batch conflict...");

        List<Timetable> batchConflicts =
                timetableRepository.updateBatchConflicts(
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


        // Classroom Conflict Check

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
                    "Classroom already has a scheduled class during this time."
            );
        }


        logger.info("Updating timetable with id: {}", id);

        Timetable savedTimetable = timetableRepository.save(timetable);

        logger.info("Timetable updated successfully with id: {}",
                savedTimetable.getTimetableId());

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


    // =========================
    // DELETE TIMETABLE
    // =========================

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


    // =========================
    // ROLE-BASED ACCESS HELPER
    // =========================

    private List<Timetable> getAccessibleTimetables() {

        User currentUser = getCurrentUser();

        if (currentUser.getRole() == Role.ADMIN) {

            // ADMIN can access all timetables.
            return timetableRepository.findAll();

        } else if (currentUser.getRole() == Role.TEACHER) {

            // TEACHER can access only their own timetable.

            if (currentUser.getTeacher() == null) {
                throw new AccessDeniedException(
                        "Teacher account is not linked to a teacher record");
            }

            return timetableRepository.findByTeacher_TeacherId(
                    currentUser.getTeacher().getTeacherId());

        } else if (currentUser.getRole() == Role.STUDENT) {

            // STUDENT can access only their own batch timetable.

            if (currentUser.getBatch() == null) {
                throw new AccessDeniedException(
                        "Student account is not linked to a batch");
            }

            return timetableRepository.findByBatch_BatchId(
                    currentUser.getBatch().getBatchId());
        }

        throw new AccessDeniedException("Access denied");
    }


    // =========================
    // GET CURRENT LOGGED-IN USER
    // =========================

    private User getCurrentUser() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getName())) {

            throw new AccessDeniedException("User is not authenticated");
        }

        return userRepository.findWithTeacherAndBatchByUsername(
                authentication.getName()
        ).orElseThrow(() ->
                new AccessDeniedException("User account not found"));
    }


    private Long getCurrentTeacherId() {

        User user = getCurrentUser();

        if (user.getRole() != Role.TEACHER || user.getTeacher() == null) {
            throw new AccessDeniedException(
                    "Teacher account is not linked to a teacher record");
        }

        return user.getTeacher().getTeacherId();
    }


    private Long getCurrentBatchId() {

        User user = getCurrentUser();

        if (user.getRole() != Role.STUDENT || user.getBatch() == null) {
            throw new AccessDeniedException(
                    "Student account is not linked to a batch");
        }

        return user.getBatch().getBatchId();
    }

}
