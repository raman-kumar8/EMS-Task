package com.example.emstaskservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;
@Data
public class RequestInsertTaskDto {
    @NotBlank(message = "taskName cannot be Blank")

    private String taskName;
    @NotNull(message = "UserId cannot be blank")
    private UUID userId;
    @NotBlank(message = "Description cannot be blank")
    private String description;
    @NotBlank (message = "taskStatus is required")
    private String taskStatus;
    @NotBlank(message = "title is required")
    private String title;
    @NotBlank(message = "priority is required")
    private String priority;
    @NotBlank(message = "TaskTag is  required")
    private String taskTag;

}
