package com.example.emstaskservice;

import com.example.emstaskservice.dto.RequestInsertTaskDto;
import com.example.emstaskservice.dto.RequestListUUidsDto;
import com.example.emstaskservice.enums.Priority;
import com.example.emstaskservice.enums.TaskStatus;
import com.example.emstaskservice.exception.CustomException;
import com.example.emstaskservice.model.TaskModel;
import com.example.emstaskservice.model.TaskTagModel;
import com.example.emstaskservice.repository.TaskRepository;
import com.example.emstaskservice.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.LocalTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

import static org.mockito.Mockito.*;

class TaskServiceTest {
    @Mock
    private TaskRepository taskRepository;
    @InjectMocks
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        taskService = new TaskService(taskRepository);
    }

    @Test
    void testAddTask_ValidDto_SavesAndReturnsResponse() {
        RequestInsertTaskDto dto = new RequestInsertTaskDto();
        dto.setTaskName("Task1");
        dto.setUserId(UUID.randomUUID());
        dto.setTitle("Title");
        dto.setDescription("Desc");
        dto.setStartTime(LocalTime.now());
        dto.setTaskStatus(TaskStatus.PENDING);
        dto.setPriority(Priority.HIGH);
        dto.setTaskTag("tag");
        when(taskRepository.save(any(TaskModel.class))).thenAnswer(i -> i.getArgument(0));
        ResponseEntity<?> response = taskService.addTask(dto);
        assertNotNull(response);
        assertEquals("Task1", ((com.example.emstaskservice.dto.ResponseDto)response.getBody()).getTaskName());
    }

    @Test
    void testAddTask_StatusCompleted_ResetsToPending() {
        RequestInsertTaskDto dto = new RequestInsertTaskDto();
        dto.setTaskStatus(TaskStatus.COMPLETED);
        when(taskRepository.save(any(TaskModel.class))).thenAnswer(i -> i.getArgument(0));
        ResponseEntity<?> response = taskService.addTask(dto);
        assertEquals(TaskStatus.PENDING, ((com.example.emstaskservice.dto.ResponseDto)response.getBody()).getTaskStatus());
    }

    @Test
    void testGetTasksByUserId_ReturnsList() {
        UUID userId = UUID.randomUUID();
        List<TaskModel> tasks = Arrays.asList(new TaskModel(), new TaskModel());
        when(taskRepository.findByUserId(userId)).thenReturn(tasks);
        List<TaskModel> result = taskService.getTasksByUserId(userId);
        assertEquals(2, result.size());
    }

    @Test
    void testDeleteTask_ExistingId_Deletes() {
        UUID id = UUID.randomUUID();
        when(taskRepository.existsById(id)).thenReturn(true);
        doNothing().when(taskRepository).deleteById(id);
        assertDoesNotThrow(() -> taskService.deleteTask(id));
    }

    @Test
    void testDeleteTask_NonExistingId_ThrowsException() {
        UUID id = UUID.randomUUID();
        when(taskRepository.existsById(id)).thenReturn(false);
        assertThrows(CustomException.class, () -> taskService.deleteTask(id));
    }

    @Test
    void testFindById_Existing_ReturnsTask() {
        UUID id = UUID.randomUUID();
        TaskModel task = new TaskModel();
        when(taskRepository.findById(id)).thenReturn(Optional.of(task));
        assertEquals(task, taskService.findById(id));
    }

    @Test
    void testFindById_NotFound_ReturnsNull() {
        UUID id = UUID.randomUUID();
        when(taskRepository.findById(id)).thenReturn(Optional.empty());
        assertNull(taskService.findById(id));
    }

    @Test
    void testGetAllByTaskId_ReturnsList() {
        RequestListUUidsDto dto = new RequestListUUidsDto();
        List<UUID> uuids = Arrays.asList(UUID.randomUUID(), UUID.randomUUID());
        dto.setUuids(uuids);
        List<TaskModel> tasks = Arrays.asList(new TaskModel(), new TaskModel());
        when(taskRepository.findAllById(uuids)).thenReturn(tasks);
        List<TaskModel> result = taskService.getAllByTaskId(dto);
        assertEquals(2, result.size());
    }

    @Test
    void testUpdateTask_UpdatesFieldsAndSaves() {
        TaskModel existing = new TaskModel();
        existing.setTag(new TaskTagModel());
        RequestInsertTaskDto dto = new RequestInsertTaskDto();
        dto.setPriority(Priority.HIGH);
        dto.setTaskStatus(TaskStatus.PENDING);
        dto.setEndTime(LocalTime.now());
        dto.setDuration(LocalTime.of(1,0));
        dto.setTitle("title");
        dto.setDescription("desc");
        dto.setTaskTag("tag");
        doAnswer(i -> i.getArgument(0)).when(taskRepository).save(any(TaskModel.class));
        assertDoesNotThrow(() -> taskService.updateTask(existing, dto));
        assertEquals("tag", existing.getTag().getTag());
    }

    @Test
    void testUpdateTask_NullTag_CreatesTagModel() {
        TaskModel existing = new TaskModel();
        existing.setTag(null);
        RequestInsertTaskDto dto = new RequestInsertTaskDto();
        dto.setTaskTag("tag");
        doAnswer(i -> i.getArgument(0)).when(taskRepository).save(any(TaskModel.class));
        assertDoesNotThrow(() -> taskService.updateTask(existing, dto));
        assertNotNull(existing.getTag());
        assertEquals("tag", existing.getTag().getTag());
    }
}
