package com.example.emstaskservice.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalTime;
@Data
@AllArgsConstructor
public class ResponseDto {


    private String description;
    private String taskStatus;
    private String title;
    private String priority;
    private LocalTime startTime;

   private String taskTag;
}
