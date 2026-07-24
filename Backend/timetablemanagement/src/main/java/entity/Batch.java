package com.aacharya.timetablemanagement.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Batch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long batchId;

    @NotBlank(message = "Batch name cannot be empty")
    private String batchName;

    //public Long getBatchId() {return batchId;}

 //   public void setBatchId(Long batchId) {this.batchId = batchId;}

    //public String getBatchName() {return batchName;}

   // public void setBatchName(String batchName) {this.batchName = batchName;}
}