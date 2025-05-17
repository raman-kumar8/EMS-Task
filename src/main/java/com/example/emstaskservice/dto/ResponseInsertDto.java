package com.example.emstaskservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalTime;
import java.util.UUID;
@Data
@AllArgsConstructor
public class ResponseInsertDto {

    private String taskName;
    private String description;
    private String taskStatus;
    private String title;
    private String priority;
    private String taskTag;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalTime duration;


}
