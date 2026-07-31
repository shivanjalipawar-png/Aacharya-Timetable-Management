package com.aacharya.timetablemanagement.entity;

import jakarta.persistence.*;
import java.time.DayOfWeek;
import java.time.LocalTime;

@Entity
public class Timetable {

    public Timetable(){}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long timetableId;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @ManyToOne
    @JoinColumn(name = "batch_id")
    private Batch batch;

    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @Enumerated(EnumType.STRING)
    private  DayOfWeek day;
    private LocalTime startTime;

  private LocalTime endTime;

    private String classroom;

    public Long getTimetableId() {

        return timetableId;
    }

    public void setTimetableId(Long timetableId) {

        this.timetableId = timetableId;
    }

    public DayOfWeek getDay() {

        return day;
    }

    public void setDay(DayOfWeek day) {

        this.day = day;
    }

    public LocalTime getStartTime() {

        return startTime;
    }

    public void setStartTime(LocalTime startTime) {

        this.startTime = startTime;
    }

    public LocalTime getEndTime() {

        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public String getClassroom() {

        return classroom;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }

    public Teacher getTeacher() {

        return teacher;
    }

    public void setTeacher(Teacher teacher) {

        this.teacher = teacher;
    }

    public Batch getBatch() {

        return batch;
    }

    public void setBatch(Batch batch) {

        this.batch = batch;
    }

    public Subject getSubject() {

        return subject;
    }

    public void setSubject(Subject subject) {

        this.subject = subject;
    }
}
