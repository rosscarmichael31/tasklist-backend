package com.thg.accelerator.tasklist.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thg.accelerator.tasklist.controller.TaskController;
import com.thg.accelerator.tasklist.model.Task;
import com.thg.accelerator.tasklist.model.TaskDTO;
import com.thg.accelerator.tasklist.respository.TaskDatabaseRepository;
import com.thg.accelerator.tasklist.service.LabelService;
import com.thg.accelerator.tasklist.service.TaskDTOMapper;
import com.thg.accelerator.tasklist.service.TaskMapper;
import com.thg.accelerator.tasklist.service.TaskService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Optional;

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
    @Autowired
    TaskMapper taskmapper;
    @Autowired
    TaskDTOMapper taskDTOMapper = new TaskDTOMapper();

    ObjectMapper objectMapper = new ObjectMapper();


    @Test
    @DisplayName("it creates")
    void create() throws Exception {
        Task task = new Task("test_task", true, true, 1);
        TaskDTO taskDTO = taskDTOMapper.apply(task);

        when(taskService.create(taskDTO)).thenReturn(taskDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/tasks").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("it finds by id")
    void findById() throws Exception {
        Task task = new Task("test_task", false, true, 1);
        TaskDTO taskDTO = taskDTOMapper.apply(task);

        when(taskService.findById(1)).thenReturn(Optional.of(taskDTO));


        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/tasks/{id}", 1))
                .andExpect(status().isOk())
                .andReturn();


        String stringResult = result.getResponse().getContentAsString();
        TaskDTO taskResult = objectMapper.readValue(stringResult, TaskDTO.class);

        Assertions.assertEquals("test_task", taskResult.getDescription());

    }

    @Test
    @DisplayName("it finds all")
    void findAllDefault() throws Exception {
        when(taskService.findAll()).thenReturn(List.of(
                taskDTOMapper.apply(new Task("task 1", true, false, 1)),
                taskDTOMapper.apply(new Task("task 2", true, false, 3)),
                taskDTOMapper.apply(new Task("task 3", false, true, 2)),
                taskDTOMapper.apply(new Task("task 4", false, true, 2))
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
        when(taskService.findByPriority()).thenReturn(List.of(
                taskDTOMapper.apply(new Task("task 1", true, false, 1)),
                taskDTOMapper.apply(new Task("task 3", false, true, 2)),
                taskDTOMapper.apply(new Task("task 2", true, false, 3)),
                taskDTOMapper.apply(new Task("task 4", false, true, 1))
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
        when(taskService.findByInProgress()).thenReturn(List.of(
                taskDTOMapper.apply(new Task("task 3", false, true, 2)),
                taskDTOMapper.apply(new Task("task 4", false, true, 3)))
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
        when(taskService.findByIncomplete()).thenReturn(List.of(
                taskDTOMapper.apply(new Task("task 3", false, true, 2)),
                taskDTOMapper.apply(new Task("task 4", false, true, 3))
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