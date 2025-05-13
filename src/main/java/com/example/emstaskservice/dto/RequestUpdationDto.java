package com.example.emstaskservice.dto;

import lombok.Data;

import java.time.LocalTime;
@Data
public class RequestUpdationDto {
    LocalTime endTime;
    String priority;
    String taskStatus;


}
