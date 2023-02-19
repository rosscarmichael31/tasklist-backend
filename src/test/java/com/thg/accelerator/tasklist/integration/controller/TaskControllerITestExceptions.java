package com.thg.accelerator.tasklist.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thg.accelerator.tasklist.model.Label;
import com.thg.accelerator.tasklist.model.Task;
import com.thg.accelerator.tasklist.respository.TaskDatabaseRepository;
import com.thg.accelerator.tasklist.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class TaskControllerITestExceptions {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskDatabaseRepository taskDatabaseRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    TaskService taskService;


    @BeforeEach
    void setup() {
        taskDatabaseRepository.deleteAll();
    }


    @Test
    @DisplayName("is handles a post exception")
    public void testPostTaskException() throws Exception {
        // given
        Set<Label> labels = new HashSet<>();
        labels.add(new Label("test_label1"));
        labels.add(new Label("test_label2"));

        Task task = new Task("test_task", false, false, 1, labels);

        // when
        when(taskService.create(task)).thenThrow(new RuntimeException("Database is unavailable"));

        // then
       mockMvc.perform(post("/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content((objectMapper.writeValueAsString(task)))).andExpect(status().isInternalServerError());
    }


    @Test
    @DisplayName("it handles a findAll() exception")
    public void testGetAllTasksException() throws Exception {
        // given
        List<Task> tasks = new ArrayList<>();
        tasks.add(new Task("test_task1", false, false, 1));
        tasks.add(new Task("test_task2", true, true, 2));

        taskDatabaseRepository.saveAll(tasks);

        // when
        when(taskService.findAll()).thenThrow(new RuntimeException("Database is unavailable"));
        ResultActions response = mockMvc.perform(get("/tasks"));

        // then
        response.andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("it handles an exception in getById()")
    public void testGetTaskByIdException() throws Exception {
        // given
        Set<Label> labels = new HashSet<>();
        labels.add(new Label("test_label1"));
        labels.add(new Label("test_label2"));

        long id = 2;
        Task task = new Task("test_task", false, false, 1, labels);

        taskDatabaseRepository.save(task);

        // when
        when(taskService.findById(id)).thenThrow(new RuntimeException("Database is unavailable"));
        ResultActions response = mockMvc.perform(get("/tasks/{id}", id));

        // then
        response.andExpect(status().isInternalServerError());

    }

    @Test
    @DisplayName("it handles an exception in updateTask()")
    public void testUpdateTaskException() throws Exception {
        // given
        Task savedTask = new Task("test_task", false, false, 1);
        taskDatabaseRepository.save(savedTask);
        Task updatedTask = new Task("test_task_updated", false, false, 1);
        Long savedId = savedTask.getId();
        // when
        when(taskService.findById(savedId)).thenThrow(new RuntimeException("Database is unavailable"));

        ResultActions response = mockMvc.perform(put("/tasks/{id}", savedTask.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedTask)));

        // then
        response.andExpect(status().isInternalServerError());
    }
}
