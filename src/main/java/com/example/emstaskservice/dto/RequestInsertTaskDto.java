package com.example.emstaskservice.dto;

import com.example.emstaskservice.enums.Priority;
import com.example.emstaskservice.enums.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalTime;
import java.util.UUID;
@Data
public class RequestInsertTaskDto {
    @NotBlank(message = "taskName cannot be Blank")

    private String taskName;
    @NotNull(message = "UserId cannot be blank")
    private UUID userId;
    @NotBlank(message = "Description cannot be blank")
    private String description;

    @NotBlank(message = "title is required")
    private String title;
    @NotNull(message = "Cannot be null")
    private TaskStatus taskStatus;
    @NotNull(message = "cannot be null")
    private Priority priority;

    private String taskTag;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalTime duration;


}
