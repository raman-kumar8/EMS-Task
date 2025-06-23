package com.example.emstaskservice.controller;

import com.example.emstaskservice.openfeign.Validate;
import com.example.emstaskservice.dto.*;
import com.example.emstaskservice.enums.Priority;
import com.example.emstaskservice.enums.TaskStatus;
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
import java.util.Map;
import java.util.UUID;

@RestController
public class Controller {
    private final TaskService taskService;
    private final Validate validate;
    @Autowired
    public Controller(TaskService taskService,Validate validate){
        this.taskService = taskService;
        this.validate = validate;
    }


    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ResponseDto> add (@Valid @RequestBody RequestInsertTaskDto requestInsertTaskDto){
       return taskService.addTask(requestInsertTaskDto);

    }
    @PostMapping("/getAllbyId")
public ResponseEntity<List<TaskModel>> getAllById(@Valid @RequestBody RequestListUUidsDto requestListUUidsDto){
        return ResponseEntity.ok(taskService.getAllByTaskId(requestListUUidsDto));
    }

    @GetMapping("/getAll")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public List<TaskModel> getAll(@CookieValue("jwt_token") String token) {


        String cookieHeader = "jwt_token=" + token;

        try {
            String userIdString = validate.validate(cookieHeader);



            if (userIdString.isEmpty()) {
                throw new CustomException("Invalid token or server down");
            }

            UUID userId = UUID.fromString(userIdString);
            return taskService.getTasksByUserId(userId);
        } catch (Exception e) {

            throw new CustomException("Invalid UUID or failed to fetch tasks");
        }
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


        Priority priority = requestUpdationDto.getPriority();
        TaskStatus taskStatus = requestUpdationDto.getTaskStatus();
        LocalTime endTime = requestUpdationDto.getEndTime();

        RequestInsertTaskDto requestInsertTaskDto = new RequestInsertTaskDto();
        requestInsertTaskDto.setPriority(priority != null ? priority : taskModel.getPriority());
        requestInsertTaskDto.setTaskStatus(taskStatus != null ? taskStatus : taskModel.getTaskStatus());

        if (endTime != null) {
            requestInsertTaskDto.setEndTime(endTime);

            requestInsertTaskDto.setDuration(LocalTime.ofSecondOfDay(Duration.between(taskModel.getStartTime(), endTime).toSeconds()));

        } else {
            requestInsertTaskDto.setEndTime(taskModel.getEndTime());
            requestInsertTaskDto.setDuration(taskModel.getDuration());
        }

        requestInsertTaskDto.setUserId(taskModel.getUserId());
        requestInsertTaskDto.setTitle(taskModel.getTitle());
        requestInsertTaskDto.setDescription(taskModel.getDescription());
        requestInsertTaskDto.setStartTime(taskModel.getStartTime());
        requestInsertTaskDto.setTaskTag(taskModel.getTag().getTag());
        requestInsertTaskDto.setTaskName(taskModel.getTitle());

        taskService.updateTask(taskModel, requestInsertTaskDto);

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
                        requestInsertTaskDto.getDuration(),
                        requestInsertTaskDto.getAssignedBy()
                ),
                HttpStatus.OK
        );
    }
    @GetMapping("/getByUserId/{userId}")
    public ResponseEntity<List<TaskModel>> getTasksByUserId(@PathVariable UUID userId) {
        List<TaskModel> tasks = taskService.getTasksByUserId(userId);
        return ResponseEntity.ok(tasks);
    }


    @GetMapping("/active/count")
    public long getActiveTaskCount() {
        return taskService.countActiveTasks();
    }

    @GetMapping("/completed/count")
    public long getCompletedTaskCount() {
        return taskService.countCompletedTasks();
    }

    @GetMapping("/summary/{userId}")
    public ResponseEntity<Map<String, Object>> getTaskSummary(@PathVariable UUID userId) {
        Map<String, Object> summary = taskService.getTaskSummary(userId);
        return ResponseEntity.ok(summary);
    }



}
