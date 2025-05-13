package com.example.emstaskservice.service;

import com.example.emstaskservice.dto.RequestInsertTaskDto;
import com.example.emstaskservice.dto.ResponseDto;
import com.example.emstaskservice.model.TaskModel;
import com.example.emstaskservice.model.TaskTagModel;
import com.example.emstaskservice.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Service
public class TaskService {
    private  final TaskRepository taskRepository;
    @Autowired
    TaskService(TaskRepository taskRepository){
        this.taskRepository = taskRepository;
    }
    @ResponseStatus
    public ResponseEntity<ResponseDto> addTask(RequestInsertTaskDto requestInsertTaskDto){

        TaskModel taskModel = new TaskModel();
        taskModel.setUser_id(requestInsertTaskDto.getUserId());
        taskModel.setTitle(requestInsertTaskDto.getTitle());
        taskModel.setDescription(requestInsertTaskDto.getDescription());
        taskModel.setStart_time(LocalTime.now());
        taskModel.setTaskStatus("pending");
        taskModel.setPriority(requestInsertTaskDto.getPriority());


        TaskTagModel taskTagModel = new TaskTagModel();
        taskTagModel.setTag(requestInsertTaskDto.getTaskTag());
        taskTagModel.setTask(taskModel); // Set owning side

        taskModel.setTag(taskTagModel);


        taskRepository.save(taskModel);


        return ResponseEntity.ok(
                new ResponseDto(
                        taskModel.getDescription(),
                        taskModel.getTaskStatus(),
                        taskModel.getTitle(),
                        taskModel.getPriority(),
                        taskModel.getStart_time(),
                        taskModel.getTag().getTag()
                )
        );
    }
    public List<TaskModel> getTasksByUserId(UUID userId) {
        return taskRepository.findByUserId(userId);
    }
   public void deleteTask(UUID id){
        taskRepository.deleteById(id);
   }
   public TaskModel findById(UUID id){
       return taskRepository.findById(id).orElse(null);
   }


}
