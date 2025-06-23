package com.example.emstaskservice.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data
public class TaskTagModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "text")
    private String tag;

    @OneToOne
    @JoinColumn(name = "task_id", nullable = false)
    @JsonBackReference
    private TaskModel task;
}

