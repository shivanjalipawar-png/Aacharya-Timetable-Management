package com.aacharya.timetablemanagement.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


public class SubjectRequestDTO {

    @NotBlank(message = "Subject name is required")
 private  String subjectName;

    @NotBlank(message = "Subject code is required")
 private String subjectCode;

    @NotNull(message = "Teacher is required")
    private Long teacherId;

    @NotNull(message = "Credits are required")
    private Integer credits;

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public Long getTeacherId() {
        return teacherId;
    }

    public Integer getCredits() {
        return credits;
    }

    public void setCredits(Integer credits) {
        this.credits = credits;
    }

    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
    }

}
