package com.example.emstaskservice.controller;

import com.example.emstaskservice.dto.RequestInsertTaskDto;
import com.example.emstaskservice.dto.ResponseDto;
import com.example.emstaskservice.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {
    private final TaskService taskService;
    @Autowired
    public Controller(TaskService taskService){
        this.taskService = taskService;
    }
    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ResponseDto> add (@Valid @RequestBody RequestInsertTaskDto requestInsertTaskDto){
       return taskService.addTask(requestInsertTaskDto);

    }

}
