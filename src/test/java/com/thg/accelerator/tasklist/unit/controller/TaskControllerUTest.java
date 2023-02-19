package com.thg.accelerator.tasklist.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thg.accelerator.tasklist.model.Label;
import com.thg.accelerator.tasklist.model.Task;
import com.thg.accelerator.tasklist.respository.TaskDatabaseRepository;
import com.thg.accelerator.tasklist.service.LabelService;
import com.thg.accelerator.tasklist.service.TaskService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerUTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    TaskService taskService;
    @MockBean
    LabelService labelService;
    @MockBean
    TaskDatabaseRepository taskDatabaseRepository;

    ObjectMapper objectMapper = new ObjectMapper();


    @Test
    @DisplayName("it creates")
    void create() throws Exception {
        Set<Label> labels = new HashSet<>();
        labels.add(new Label("test_label1"));
        labels.add(new Label("test_label2"));

        Task task = new Task("test_task", true, true, 1, labels);

        when(taskService.create(task)).thenReturn(task);

        mockMvc.perform(MockMvcRequestBuilders.post("/tasks").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("it returns internal server error")
    void createException() throws Exception {
        Set<Label> labels = new HashSet<>();
        labels.add(new Label("test_label1"));
        labels.add(new Label("test_label2"));

        Task task = new Task("test_task", true, true, 1, labels);

        when(taskService.create(task)).thenThrow(new RuntimeException("Internal server error"));

        mockMvc.perform(MockMvcRequestBuilders.post("/tasks").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("it finds by id")
    void findById() throws Exception {
        Set<Label> labels = new HashSet<>();
        labels.add(new Label("test_label1"));
        labels.add(new Label("test_label2"));

        Task task = new Task("test_task", false, true, 1, labels);

        when(taskService.findById(1)).thenReturn(Optional.of(task));


        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/tasks/{id}", 1))
                .andExpect(status().isOk())
                .andReturn();


        String stringResult = result.getResponse().getContentAsString();
        Task taskResult = objectMapper.readValue(stringResult, Task.class);

        Assertions.assertEquals("test_task", taskResult.getDescription());

    }

    @Test
    @DisplayName("it returns internal server error")
    void findByIdException() throws Exception {
        when(taskService.findById(1)).thenThrow(new RuntimeException("Internal server error"));
        mockMvc.perform(MockMvcRequestBuilders.get("/tasks/{id}", 1))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("it finds all")
    void findAllDefault() throws Exception {
        Set<Label> labels = new HashSet<>();
        labels.add(new Label("test_label1"));
        labels.add(new Label("test_label2"));

        when(taskService.findAll()).thenReturn(List.of(
                new Task("task 1", true, false, 1, labels),
                new Task("task 2", true, false, 3, labels),
                new Task("task 3", false, true, 2, labels),
                new Task("task 4", false, true, 2, labels)
        ));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/tasks"))
                .andExpect(status().isOk())
                .andReturn();

        String stringResult = result.getResponse().getContentAsString();
        List listResult = objectMapper.readValue(stringResult, List.class);

        Assertions.assertEquals(4, listResult.size());

    }

    @Test
    @DisplayName("it finds by priority")
    void findAllSortByPriority() throws Exception {
        Set<Label> labels = new HashSet<>();
        labels.add(new Label("test_label1"));
        labels.add(new Label("test_label2"));

        when(taskService.findByPriority()).thenReturn(List.of(
                new Task("task 1", true, false, 1, labels),
                new Task("task 3", false, true, 2, labels),
                new Task("task 2", true, false, 3, labels),
                new Task("task 4", false, true, 1, labels)
        ));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/tasks?sortBy=priority"))
                .andExpect(status().isOk())
                .andReturn();

        String stringResult = result.getResponse().getContentAsString();
        List<Task> listResult = objectMapper.readValue(stringResult, List.class);

        Assertions.assertEquals(4, listResult.size());
    }

    @Test
    @DisplayName("it finds all in progress")
    void findAllInProgress() throws Exception {
        Set<Label> labels = new HashSet<>();
        labels.add(new Label("test_label1"));
        labels.add(new Label("test_label2"));

        when(taskService.findByInProgress()).thenReturn(List.of(
                new Task("task 3", false, true, 2, labels),
                new Task("task 4", false, true, 3, labels))
        );

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/tasks?sortBy=in-progress"))
                .andExpect(status().isOk())
                .andReturn();

        String stringResult = result.getResponse().getContentAsString();
        List listResult = objectMapper.readValue(stringResult, List.class);

        Assertions.assertEquals(2, listResult.size());
    }

    @Test
    @DisplayName("it finds all incomplete")
    void findAllIncomplete() throws Exception {
        Set<Label> labels = new HashSet<>();
        labels.add(new Label("test_label1"));
        labels.add(new Label("test_label2"));

        when(taskService.findByIncomplete()).thenReturn(List.of(
                new Task("task 3", false, true, 2, labels),
                new Task("task 4", false, true, 3, labels)
        ));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/tasks?sortBy=incomplete"))
                .andExpect(status().isOk())
                .andReturn();

        String stringResult = result.getResponse().getContentAsString();
        List listResult = objectMapper.readValue(stringResult, List.class);

        Assertions.assertEquals(2, listResult.size());
    }

    @Test
    @DisplayName("it returns all tasks if sort param is illegal")
    void findAllIllegalArgumentException() throws Exception {
        Set<Label> labels = new HashSet<>();
        labels.add(new Label("test_label1"));
        labels.add(new Label("test_label2"));

        when(taskService.findAll()).thenReturn(List.of(
                new Task("task 1", true, false, 1, labels),
                new Task("task 2", true, false, 3, labels),
                new Task("task 3", false, true, 2, labels),
                new Task("task 4", false, true, 2, labels)
        ));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/tasks?sortBy=INVALID"))
                .andExpect(status().isOk())
                .andReturn();

        String stringResult = result.getResponse().getContentAsString();
        List listResult = objectMapper.readValue(stringResult, List.class);

        Assertions.assertEquals(4, listResult.size());
    }

    @Test
    @DisplayName("it returns internal server error")
    void findAllException() throws Exception {
        when(taskService.findByPriority()).thenThrow(new RuntimeException("Internal server error"));
        when(taskService.findByInProgress()).thenThrow(new RuntimeException("Internal server error"));
        when(taskService.findByIncomplete()).thenThrow(new RuntimeException("Internal server error"));
        when(taskService.findAll()).thenThrow(new RuntimeException("Internal server error"));

        mockMvc.perform(MockMvcRequestBuilders.get("/tasks?sortBy=priority", 1))
                .andExpect(status().isInternalServerError());
        mockMvc.perform(MockMvcRequestBuilders.get("/tasks?sortBy=in-progress", 1))
                .andExpect(status().isInternalServerError());
        mockMvc.perform(MockMvcRequestBuilders.get("/tasks?sortBy=incomplete", 1))
                .andExpect(status().isInternalServerError());
        mockMvc.perform(MockMvcRequestBuilders.get("/tasks?sortBy=INVALID", 1))
                .andExpect(status().isInternalServerError());
    }


    @Test
    void update() {
    }

    @Test
    void delete() {
    }
}