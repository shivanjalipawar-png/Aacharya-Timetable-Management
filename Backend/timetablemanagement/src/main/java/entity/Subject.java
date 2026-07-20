package com.aacharya.timetablemanagement.entity;

import com.aacharya.timetablemanagement.entity.Teacher;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Id;
import jakarta.persistence.GenerationType;
//import jakarta.persistence.Teacher;
@Entity
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long subjectId;

    private String subjectName;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    public long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(long subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

}