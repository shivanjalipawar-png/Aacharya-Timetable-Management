package com.aacharya.timetablemanagement.dto;

import jakarta.validation.constraints.NotBlank;

public class UserRequestDTO {

    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Password is required")
    private String password;

    public UserRequestDTO(){ }

    public UserRequestDTO(String username , String password){
        this.password=password;
        this.username= username;

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
