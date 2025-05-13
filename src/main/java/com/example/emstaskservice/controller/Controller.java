package com.example.emstaskservice.controller;

import com.example.emstaskservice.OpenFeign.Validate;
import com.example.emstaskservice.dto.RequestInsertTaskDto;
import com.example.emstaskservice.dto.RequestUpdationDto;
import com.example.emstaskservice.dto.ResponseDto;
import com.example.emstaskservice.dto.ResponseInsertDto;
import com.example.emstaskservice.exception.CustomException;
import com.example.emstaskservice.model.TaskModel;
import com.example.emstaskservice.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalTime;
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
    @DeleteMapping("/delete/{id}")
    public boolean delete(@PathVariable UUID id){

        taskService.deleteTask(id);

 return true;
    }
    @PutMapping("/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ResponseInsertDto> update(@RequestBody RequestUpdationDto requestUpdationDto, @PathVariable UUID id) {
        TaskModel taskModel = taskService.findById(id);
        if (taskModel == null) {
            throw new CustomException("Task not found");
        }

        String priority = requestUpdationDto.getPriority();
        String taskStatus = requestUpdationDto.getTaskStatus();
        LocalTime endTime = requestUpdationDto.getEndTime();

        RequestInsertTaskDto requestInsertTaskDto = new RequestInsertTaskDto();
        if(priority!=null){
            requestInsertTaskDto.setPriority(priority);
        }else{
            requestInsertTaskDto.setPriority(taskModel.getPriority());
        }
        if(taskStatus!=null){
            requestInsertTaskDto.setTaskStatus(taskStatus);

        }else{
            requestInsertTaskDto.setTaskStatus(taskModel.getTaskStatus());
        }
        if(endTime!=null){
            requestInsertTaskDto.setEndTime(endTime);
        }else {
            requestInsertTaskDto.setEndTime(taskModel.getEnd_time());
        }


        requestInsertTaskDto.setUserId(taskModel.getUser_id());
        requestInsertTaskDto.setTitle(taskModel.getTitle());
        requestInsertTaskDto.setDescription(taskModel.getDescription());
        requestInsertTaskDto.setStartTime(taskModel.getStart_time());
        requestInsertTaskDto.setTaskTag(taskModel.getTag().getTag());
        requestInsertTaskDto.setDuration(LocalTime.ofSecondOfDay(Duration.between(taskModel.getStart_time(), endTime).toMinutes()));
        requestInsertTaskDto.setTaskName(taskModel.getTitle());

        taskService.addTask(requestInsertTaskDto);
     return new ResponseEntity<>(
             new ResponseInsertDto(
                     requestInsertTaskDto.getTaskName(),
                     requestInsertTaskDto.getDescription(),
                     requestInsertTaskDto.getTaskStatus(),
                     requestInsertTaskDto.getTitle(),
                     requestInsertTaskDto.getPriority(),
                     requestInsertTaskDto.getTaskTag(),
                     requestInsertTaskDto.getStartTime(),
                     requestInsertTaskDto.getEndTime(),
                     requestInsertTaskDto.getDuration()
             )
    ,HttpStatus.OK );
    }


}
