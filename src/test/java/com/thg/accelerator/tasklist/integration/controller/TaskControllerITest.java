package com.thg.accelerator.tasklist.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thg.accelerator.tasklist.model.Label;
import com.thg.accelerator.tasklist.model.Task;
import com.thg.accelerator.tasklist.respository.LabelDatabaseRepository;
import com.thg.accelerator.tasklist.respository.TaskDatabaseRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class TaskControllerITest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskDatabaseRepository taskDatabaseRepository;

    @Autowired
    private LabelDatabaseRepository labelDatabaseRepository;

    @Autowired
    private ObjectMapper objectMapper;


    @BeforeEach
    void setup() {
        taskDatabaseRepository.deleteAll();
    }

    @Test
    @DisplayName("it posts")
    public void testPostTask() throws Exception {
        // given
        Set<Label> labels = new HashSet<>();
        labels.add(new Label("test_label1"));
        labels.add(new Label("test_label2"));

        Task task = new Task("test_task", false, false, 1, labels);


        // when
        ResultActions response = mockMvc.perform(post("/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content((objectMapper.writeValueAsString(task))));

        // then
        response.andDo(print()).
                andExpect(status().isCreated())
                .andExpect(jsonPath("$.description",
                        is(task.getDescription())))
                .andExpect(jsonPath("$.complete",
                        is(task.isComplete())))
                .andExpect(jsonPath("$.inProgress",
                        is(task.isInProgress())))
                .andExpect(jsonPath("$.priority",
                        is(task.getPriority())))
                .andExpect(jsonPath("$.labels[*].name", containsInAnyOrder("test_label1", "test_label2")));
    }

    @Test
    @DisplayName("it finds all")
    public void testGetAllTasks() throws Exception {
        // given
        List<Task> tasks = new ArrayList<>();
        tasks.add(new Task("test_task1", false, false, 1));
        tasks.add(new Task("test_task2", true, true, 2));

        taskDatabaseRepository.saveAll(tasks);

        // when
        ResultActions response = mockMvc.perform(get("/tasks"));

        // then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()",
                        is(tasks.size())));
    }

    @Test
    @DisplayName("it finds by id when exists")
    public void testGetTaskByIdPositive() throws Exception {
        // given
        Set<Label> labels = new HashSet<>();
        labels.add(new Label("test_label1"));
        labels.add(new Label("test_label2"));
        Task task = new Task("test_task", false, false, 1, labels);
        taskDatabaseRepository.save(task);

        // when
        ResultActions response = mockMvc.perform(get("/tasks/{id}", task.getId()));

        // then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.description", is(task.getDescription())))
                .andExpect(jsonPath("$.complete", is(task.isComplete())))
                .andExpect(jsonPath("$.inProgress", is(task.isInProgress())))
                .andExpect(jsonPath("$.priority", is(task.getPriority())));
    }

    @Test
    @DisplayName("it sends 404 when not found")
    public void testGetTaskByIdNegative() throws Exception {
        // given
        Set<Label> labels = new HashSet<>();
        labels.add(new Label("test_label1"));
        labels.add(new Label("test_label2"));

        long id = 2;
        Task task = new Task("test_task", false, false, 1, labels);

        taskDatabaseRepository.save(task);

        // when
        ResultActions response = mockMvc.perform(get("/tasks/{id}", id));

        // then
        response.andExpect(status().isNotFound())
                .andDo(print());
    }


    @Test
    @DisplayName("it updates a tasks when it exists")
    public void testUpdateTaskPositive() throws Exception {
        // given
        Task savedTask = new Task("test_task", false, false, 1);
        taskDatabaseRepository.save(savedTask);
        Task updatedTask = new Task("test_task_updated", true, true, 2);

        // when
        ResultActions response = mockMvc.perform(put("/tasks/{id}", savedTask.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedTask)));

        // then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.description", is(updatedTask.getDescription())))
                .andExpect(jsonPath("$.complete", is(updatedTask.isComplete())))
                .andExpect(jsonPath("$.inProgress", is(updatedTask.isInProgress())))
                .andExpect(jsonPath("$.priority", is(updatedTask.getPriority())));
    }

    @Test
    @DisplayName("it sends 404 when not found")
    public void testUpdateTaskNegative() throws Exception {
        // given
        long id = 53L;
        Task savedTask = new Task("test_task", false, false, 1);
        taskDatabaseRepository.save(savedTask);
        Task updatedTask = new Task("test_task_updated", false, false, 1);

        // when
        ResultActions response = mockMvc.perform(put("/tasks/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedTask)));

        // then
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("it deleted a task")
    public void testDeleteTask() throws Exception {
        // given
        Set<Label> labels = new HashSet<>();
        labels.add(new Label("test_label1"));
        labels.add(new Label("test_label2"));
        Task task = new Task("test_task", false, false, 1, labels);
        taskDatabaseRepository.save(task);

        List<Long> labelIds = task.getLabels().stream().map(Label::getId).toList();

        // when
        ResultActions response = mockMvc.perform(delete("/tasks/{id}", task.getId()));

        // then
        response.andExpect(status().isOk())
                .andDo(print());

        labelIds.forEach(id -> Assertions.assertTrue(labelDatabaseRepository.findById(id).isEmpty()));
    }

}
