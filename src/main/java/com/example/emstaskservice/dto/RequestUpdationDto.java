package com.example.emstaskservice.dto;

import com.example.emstaskservice.enums.Priority;
import com.example.emstaskservice.enums.TaskStatus;
import lombok.Data;

import java.time.LocalTime;
@Data
public class RequestUpdationDto {
    LocalTime endTime;
    Priority priority;
    TaskStatus taskStatus;


}
