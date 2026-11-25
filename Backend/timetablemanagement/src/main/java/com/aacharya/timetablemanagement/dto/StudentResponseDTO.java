
package com.aacharya.timetablemanagement.dto;

public class StudentResponseDTO {

    private Long studentId;
    private String name;
    private String email;
    private String phone;
    private String username;
    private Long batchId;
    private String batchName;

    // Default constructor
    public StudentResponseDTO() {
    }

    // Parameterized constructor
    public StudentResponseDTO(Long studentId, String name, String email,
                              String phone, String username,
                              Long batchId, String batchName) {
        this.studentId = studentId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.username = username;
        this.batchId = batchId;
        this.batchName = batchName;
    }

    // Getters and Setters

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getBatchId() {
        return batchId;
    }

    public void setBatchId(Long batchId) {
        this.batchId = batchId;
    }

    public String getBatchName() {
        return batchName;
    }

    public void setBatchName(String batchName) {
        this.batchName = batchName;
    }
}
