package com.thg.accelerator.tasklist.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thg.accelerator.tasklist.controller.TaskController;
import com.thg.accelerator.tasklist.model.Task;
import com.thg.accelerator.tasklist.respository.TaskDatabaseRepository;
import com.thg.accelerator.tasklist.service.TaskService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
@AutoConfigureMockMvc
class TaskControllerUTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    TaskService taskService;
    @MockBean
    TaskDatabaseRepository taskDatabaseRepository;

    ObjectMapper objectMapper = new ObjectMapper();


    @Test
    void create() throws Exception {
        Task task = new Task("test_task", true, true, 1);
        when(taskService.create(task)).thenReturn(task);

        mockMvc.perform(MockMvcRequestBuilders.post("/tasks").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isCreated());

    }

    @Test
    void findById() throws Exception {

        Task task = new Task("test_task", false, true, 1);
        when(taskService.findById(1)).thenReturn(Optional.of(task));


        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/tasks/{id}", 1))
                .andExpect(status().isOk())
                .andReturn();


        String stringResult = result.getResponse().getContentAsString();
        Task taskResult = objectMapper.readValue(stringResult, Task.class);

        Assertions.assertEquals("test_task", taskResult.getDescription());

    }

    @Test
    void findAllDefault() throws Exception {
        when(taskService.findAll()).thenReturn(List.of(
                new Task("task 1", true, false, 1),
                new Task("task 2", true, false, 3),
                new Task("task 3", false, true, 2),
                new Task("task 4", false, true, 2)
        ));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/tasks"))
                .andExpect(status().isOk())
                .andReturn();

        String stringResult = result.getResponse().getContentAsString();
        List listResult = objectMapper.readValue(stringResult, List.class);

        Assertions.assertEquals(4, listResult.size());

    }

    @Test
    void findAllSorted() throws Exception {
        when(taskService.findByPriority()).thenReturn(List.of(
                new Task("task 1", true, false, 1),
                new Task("task 3", false, true, 2),
                new Task("task 2", true, false, 3),
                new Task("task 4", false, true, 1)
        ));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/tasks?sortBy=priority"))
                .andExpect(status().isOk())
                .andReturn();

        String stringResult = result.getResponse().getContentAsString();
        List<Task> listResult = objectMapper.readValue(stringResult, List.class);

        Assertions.assertEquals(4, listResult.size());
    }

    @Test
    void findAllInProgress() throws Exception {
        when(taskService.findByInProgress()).thenReturn(List.of(
                new Task("task 3", false, true, 2),
                new Task("task 4", false, true, 3)
        ));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/tasks?sortBy=in-progress"))
                .andExpect(status().isOk())
                .andReturn();

        String stringResult = result.getResponse().getContentAsString();
        List listResult = objectMapper.readValue(stringResult, List.class);

        Assertions.assertEquals(2, listResult.size());
    }

    @Test
    void findAllIncomplete() throws Exception {
        when(taskService.findByIncomplete()).thenReturn(List.of(
                new Task("task 3", false, true, 2),
                new Task("task 4", false, true, 3)
        ));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/tasks?sortBy=incomplete"))
                .andExpect(status().isOk())
                .andReturn();

        String stringResult = result.getResponse().getContentAsString();
        List listResult = objectMapper.readValue(stringResult, List.class);

        Assertions.assertEquals(2, listResult.size());
    }


    @Test
    void update() {
    }

    @Test
    void delete() {
    }
}