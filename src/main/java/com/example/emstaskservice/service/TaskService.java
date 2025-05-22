package com.example.emstaskservice.service;

import com.example.emstaskservice.dto.RequestInsertTaskDto;
import com.example.emstaskservice.dto.RequestListUUidsDto;
import com.example.emstaskservice.dto.ResponseDto;
import com.example.emstaskservice.enums.TaskStatus;
import com.example.emstaskservice.exception.CustomException;
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
        taskModel.setTaskName(requestInsertTaskDto.getTaskName());
        taskModel.setUser_id(requestInsertTaskDto.getUserId());
        taskModel.setTitle(requestInsertTaskDto.getTitle());
        taskModel.setDescription(requestInsertTaskDto.getDescription());
        taskModel.setStart_time(LocalTime.now());
        taskModel.setTaskStatus(TaskStatus.PENDING);
        taskModel.setPriority(requestInsertTaskDto.getPriority());


        TaskTagModel taskTagModel = new TaskTagModel();
        taskTagModel.setTag(requestInsertTaskDto.getTaskTag());
        taskTagModel.setTask(taskModel); // Set owning side

        taskModel.setTag(taskTagModel);


        taskRepository.save(taskModel);


        return ResponseEntity.ok(
                new ResponseDto(
                        taskModel.getTaskName(),
                        taskModel.getId(),
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
        if(!taskRepository.existsById(id)){
            throw new CustomException("Task not found");
        }
        taskRepository.deleteById(id);
   }
   public TaskModel findById(UUID id){
       return taskRepository.findById(id).orElse(null);
   }

   public List<TaskModel> getAllByTaskId(RequestListUUidsDto requestListUUidsDto){
        return  taskRepository.findAllById(requestListUUidsDto.getUuids());
   }

    public void updateTask(TaskModel existingTask, RequestInsertTaskDto updatedDto) {
        existingTask.setPriority(updatedDto.getPriority());
        existingTask.setTaskStatus(updatedDto.getTaskStatus());
        existingTask.setEnd_time(updatedDto.getEndTime());


        existingTask.setDuration(updatedDto.getDuration());

        // Optional updates
        existingTask.setTitle(updatedDto.getTitle());
        existingTask.setDescription(updatedDto.getDescription());

        // Handle tag
        TaskTagModel tagModel = existingTask.getTag();
        if (tagModel == null) {
            tagModel = new TaskTagModel();
            tagModel.setTask(existingTask);
        }
        tagModel.setTag(updatedDto.getTaskTag());
        existingTask.setTag(tagModel);


        taskRepository.save(existingTask);
    }




}
