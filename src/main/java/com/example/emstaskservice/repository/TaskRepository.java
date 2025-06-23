package com.example.emstaskservice.repository;

import com.example.emstaskservice.enums.TaskStatus;
import com.example.emstaskservice.model.TaskModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<TaskModel, UUID> {
    @Query("SELECT t FROM TaskModel t WHERE t.userId = :userId")
    public List<TaskModel> findByUserId(@Param("userId") UUID userId);

    long countByTaskStatusIn(List<TaskStatus> statuses);

    @Query("SELECT COUNT(t) FROM TaskModel t WHERE t.userId = :userId AND t.taskStatus = :taskStatus")
    Long countByUserIdAndTaskStatus(@Param("userId") UUID userId, @Param("taskStatus") TaskStatus taskStatus);

    @Query("SELECT COUNT(t) FROM TaskModel t WHERE t.userId = :userId AND t.taskStatus IN :taskStatuses")
    Long countByUserIdAndTaskStatusIn(@Param("userId") UUID userId, @Param("taskStatuses") List<TaskStatus> taskStatuses);



}
