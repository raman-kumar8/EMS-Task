package com.example.emstaskservice.repository;

import com.example.emstaskservice.model.TaskTagModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskTagRepository extends JpaRepository<TaskTagModel,Long> {

}
