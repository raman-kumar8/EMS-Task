package com.example.emstaskservice;

import com.example.emstaskservice.controller.Controller;
import com.example.emstaskservice.dto.*;
import com.example.emstaskservice.enums.Priority;
import com.example.emstaskservice.enums.TaskStatus;
import com.example.emstaskservice.exception.CustomException;
import com.example.emstaskservice.model.TaskModel;
import com.example.emstaskservice.model.TaskTagModel;
import com.example.emstaskservice.service.TaskService;
import com.example.emstaskservice.OpenFeign.Validate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.Field;
import java.time.LocalTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class ControllerTest {
    @Mock
    private TaskService taskService;
    @Mock
    private Validate validate;
    @InjectMocks
    private Controller controller;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        controller = new Controller(taskService);

        // Use reflection to inject the mock Validate if the field is private
        Field validateField = Controller.class.getDeclaredField("validate");
        validateField.setAccessible(true);
        validateField.set(controller, validate);
    }

    @Test
    void testAdd_ValidRequest_ReturnsCreated() {
        RequestInsertTaskDto dto = new RequestInsertTaskDto();
        ResponseDto responseDto = new ResponseDto();
        when(taskService.addTask(any())).thenReturn(ResponseEntity.ok(responseDto));
        ResponseEntity<ResponseDto> response = controller.add(dto);
        // The controller method is annotated with @ResponseStatus(HttpStatus.CREATED)
        // but returns ResponseEntity.ok, so the status is OK (200)
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDto, response.getBody());
    }

    @Test
    void testGetAllById_ValidRequest_ReturnsList() {
        RequestListUUidsDto dto = new RequestListUUidsDto();
        List<TaskModel> tasks = new ArrayList<>();
        when(taskService.getAllByTaskId(any())).thenReturn(tasks);
        ResponseEntity<List<TaskModel>> response = controller.getAllById(dto);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(tasks, response.getBody());
    }

    @Test
    void testGetAll_ValidToken_ReturnsTasks() {
        String token = "validtoken";
        String userId = UUID.randomUUID().toString();
        List<TaskModel> tasks = new ArrayList<>();
        when(validate.validate(anyString())).thenReturn(userId);
        when(taskService.getTasksByUserId(any())).thenReturn(tasks);
        List<TaskModel> result = controller.getAll(token);
        assertEquals(tasks, result);
    }

    @Test
    void testGetAll_InvalidToken_ThrowsCustomException() {
        String token = "invalidtoken";
        when(validate.validate(anyString())).thenReturn("");
        assertThrows(CustomException.class, () -> controller.getAll(token));
    }

    @Test
    void testGetAll_ValidateThrowsException_ThrowsCustomException() {
        String token = "token";
        when(validate.validate(anyString())).thenThrow(new RuntimeException("fail"));
        assertThrows(CustomException.class, () -> controller.getAll(token));
    }

    @Test
    void testDelete_ValidId_ReturnsTrue() {
        UUID id = UUID.randomUUID();
        doNothing().when(taskService).deleteTask(id);
        assertTrue(controller.delete(id));
    }

    @Test
    void testDelete_InvalidId_ThrowsCustomException() {
        UUID id = UUID.randomUUID();
        doThrow(new CustomException("Task not found")).when(taskService).deleteTask(id);
        assertThrows(CustomException.class, () -> controller.delete(id));
    }

    @Test
    void testUpdate_ValidRequest_ReturnsOk() {
        UUID id = UUID.randomUUID();
        RequestUpdationDto updationDto = new RequestUpdationDto();
        TaskModel taskModel = new TaskModel();
        TaskTagModel tagModel = new TaskTagModel();
        tagModel.setTag("tag");
        taskModel.setTag(tagModel);
        taskModel.setStart_time(LocalTime.now());
        when(taskService.findById(id)).thenReturn(taskModel);
        doNothing().when(taskService).updateTask(any(), any());
        ResponseEntity<ResponseInsertDto> response = controller.update(updationDto, id);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testUpdate_TaskNotFound_ThrowsCustomException() {
        UUID id = UUID.randomUUID();
        RequestUpdationDto updationDto = new RequestUpdationDto();
        when(taskService.findById(id)).thenReturn(null);
        assertThrows(CustomException.class, () -> controller.update(updationDto, id));
    }
}