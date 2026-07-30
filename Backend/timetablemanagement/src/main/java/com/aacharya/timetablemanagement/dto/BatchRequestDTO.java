package com.aacharya.timetablemanagement.dto;

import jakarta.validation.constraints.NotBlank;


public class BatchRequestDTO {

    @NotBlank(message = "Batch name is required ")
    private String batchName;



   @NotBlank(message = "Course is required")
  private String course;

    public String getBatchName() {
        return batchName;
    }

    public void setBatchName(String batchName) {
        this.batchName = batchName;
    }



    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }
}
