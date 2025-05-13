package com.example.emstaskservice.repository;

import com.example.emstaskservice.model.TaskModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<TaskModel, UUID> {
    @Query("SELECT t FROM TaskModel t WHERE t.user_id = :userId")
    public List<TaskModel> findByUserId(@Param("userId") UUID userId);


}
