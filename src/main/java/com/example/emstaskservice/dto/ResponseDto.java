package com.example.emstaskservice.dto;


import com.example.emstaskservice.enums.Priority;
import com.example.emstaskservice.enums.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDto {
    private  String taskName;
    private UUID taskId;
    private String description;
    private TaskStatus taskStatus;
    private String title;
    private Priority priority;
    private LocalTime startTime;

   private String taskTag;
}
