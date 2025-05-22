package com.example.emstaskservice.dto;

import com.example.emstaskservice.enums.Priority;
import com.example.emstaskservice.enums.TaskStatus;
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
    private TaskStatus taskStatus;
    private String title;
    private Priority priority;
    private String taskTag;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalTime duration;


}
