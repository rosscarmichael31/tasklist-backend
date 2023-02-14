package com.thg.accelerator.tasklist.unit.service;

import com.thg.accelerator.tasklist.model.Task;
import com.thg.accelerator.tasklist.respository.TaskDatabaseRepository;
import com.thg.accelerator.tasklist.service.TaskService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;


@WebMvcTest(TaskService.class)
@AutoConfigureMockMvc
class TaskServiceUTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    TaskService taskService;
    @MockBean
    TaskDatabaseRepository taskDatabaseRepository;

    @Test
    @DisplayName("it saves a task to the repo")
    void create() {

        // Given
        Task task = new Task("Test task", false, true, 1);
        when(taskDatabaseRepository.save(task)).thenReturn(task);

        // When
        Task createdTask = taskService.create(task);

        // Then
        Assertions.assertEquals(createdTask, task);}



    @Test
    @DisplayName("it returns the task with matching id")
    void findById() {
        // Given
        Task task = new Task("Test task", false, true, 1);
        long taskId = 1;
        when(taskDatabaseRepository.findById(taskId)).thenReturn(Optional.of(task));

        // When
        Optional<Task> foundTask = taskService.findById(taskId);

        // Then
        Assertions.assertTrue(foundTask.isPresent());
        Assertions.assertEquals(foundTask.get(), task);


    }

    @Test
    @DisplayName("it finds all the tasks in the repo")
    void findAll() {
        // Given
        Task task1 = new Task("Test task 1", false, true, 1);
        Task task2 = new Task("Test task 2", false, false, 2);
        when(taskDatabaseRepository.findAll()).thenReturn(Arrays.asList(task1, task2));

        // When
        List<Task> allTasks = taskService.findAll();

        // Then
        Assertions.assertArrayEquals(new Task[]{task1, task2}, allTasks.toArray());
    }

    @Test
    @DisplayName("it finds all tasks by priority")
    void findByPriority() {
        // Given
        Task task1 = new Task("Test task 1", false, true, 1);
        Task task2 = new Task("Test task 2", false, false, 2);
        Task task3 = new Task("Test task 3", false, true, 3);
        when(taskDatabaseRepository.findAll()).thenReturn(Arrays.asList(task1, task2, task3));

        // When
        List<Task> tasksByPriority = taskService.findByPriority();

        // Then
        Assertions.assertArrayEquals(new Task[]{task3, task2, task1}, tasksByPriority.toArray());
    }

    @Test
    @DisplayName("it finds all tasks in progress")
    void findByInProgress() {
        // Given
        Task task1 = new Task("Test task 1", false, true, 1);
        Task task2 = new Task("Test task 2", false, false, 2);
        Task task3 = new Task("Test task 3", true, true, 3);
        when(taskDatabaseRepository.findAll()).thenReturn(Arrays.asList(task1, task2, task3));

        // When
        List<Task> tasksInProgress = taskService.findByInProgress();

        // Then
        Assertions.assertArrayEquals(new Task[]{task1, task3}, tasksInProgress.toArray());
    }

    @Test
    void findByIncomplete() {
        // Given
        Task task1 = new Task("Test task 1", false, true, 1);
        Task task2 = new Task("Test task 2", false, false, 2);
        Task task3 = new Task("Test task 3", true, true, 3);
        when(taskDatabaseRepository.findAll()).thenReturn(Arrays.asList(task1, task2, task3));

        // When
        List<Task> incompleteTasks = taskService.findByIncomplete();

        // Then
        Assertions.assertArrayEquals(new Task[]{task1, task2}, incompleteTasks.toArray());
    }
}