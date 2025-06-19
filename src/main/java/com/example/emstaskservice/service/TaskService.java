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
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class TaskService {
    private  final TaskRepository taskRepository;
    @Autowired
    public TaskService(TaskRepository taskRepository){
        this.taskRepository = taskRepository;
    }
    @ResponseStatus
    public ResponseEntity<ResponseDto> addTask(RequestInsertTaskDto requestInsertTaskDto){

        TaskModel taskModel = new TaskModel();
        taskModel.setTaskName(requestInsertTaskDto.getTaskName());
        taskModel.setUser_id(requestInsertTaskDto.getUserId());
        taskModel.setTitle(requestInsertTaskDto.getTitle());
        taskModel.setDescription(requestInsertTaskDto.getDescription());

        taskModel.setStart_time(requestInsertTaskDto.getStartTime());
        taskModel.setAssignedBy(requestInsertTaskDto.getAssignedBy());
        taskModel.setTaskStatus(requestInsertTaskDto.getTaskStatus()==TaskStatus.COMPLETED?TaskStatus.PENDING:requestInsertTaskDto.getTaskStatus());
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
                        taskModel.getTag().getTag(),
                        taskModel.getAssignedBy()
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
        System.out.println("udpateDto exisint"+existingTask.getDuration());
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
    public long countActiveTasks() {
        List<TaskStatus> activeStatuses = Arrays.asList(TaskStatus.PENDING, TaskStatus.IN_PROGRESS);
        return taskRepository.countByTaskStatusIn(activeStatuses);
    }

    public long countCompletedTasks(){
        List<TaskStatus> completedStatuses = Arrays.asList(TaskStatus.COMPLETED);
        return taskRepository.countByTaskStatusIn(completedStatuses);

    }

    public Map<String, Object> getTaskSummary(UUID userId) {
        Long activeTasks = taskRepository.countByUserIdAndTaskStatusIn(
                userId,
                Arrays.asList(TaskStatus.PENDING, TaskStatus.IN_PROGRESS)
        );
        Long completedTasks = taskRepository.countByUserIdAndTaskStatus(userId, TaskStatus.COMPLETED);

        Map<String, Object> summary = new HashMap<>();
        summary.put("userId", userId);
        summary.put("activeTasks", activeTasks);
        summary.put("completedTasks", completedTasks);

        return summary;
    }








}
