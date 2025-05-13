package com.example.emstaskservice.controller;

import com.example.emstaskservice.OpenFeign.Validate;
import com.example.emstaskservice.dto.RequestInsertTaskDto;
import com.example.emstaskservice.dto.ResponseDto;
import com.example.emstaskservice.model.TaskModel;
import com.example.emstaskservice.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class Controller {
    private final TaskService taskService;
    @Autowired
    public Controller(TaskService taskService){
        this.taskService = taskService;
    }
    @Autowired
    private Validate validate;
    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ResponseDto> add (@Valid @RequestBody RequestInsertTaskDto requestInsertTaskDto){
       return taskService.addTask(requestInsertTaskDto);

    }
    @GetMapping("/getAll")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public List<TaskModel> getAll(@CookieValue("jwt_token") String token) {
        String cookieHeader = "jwt_token=" + token;

        String userIdString = validate.validate(cookieHeader); // Validate to get userId
        UUID userId = UUID.fromString(userIdString); // Convert to UUID

        return taskService.getTasksByUserId(userId); // Fetch tasks for that user
    }



}
