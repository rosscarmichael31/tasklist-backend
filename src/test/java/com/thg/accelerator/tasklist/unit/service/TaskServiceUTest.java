package com.thg.accelerator.tasklist.unit.service;

import com.thg.accelerator.tasklist.model.Label;
import com.thg.accelerator.tasklist.model.Task;
import com.thg.accelerator.tasklist.respository.TaskDatabaseRepository;
import com.thg.accelerator.tasklist.service.LabelService;
import com.thg.accelerator.tasklist.service.TaskService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.*;

import static org.mockito.Mockito.*;

@SpringBootTest
class TaskServiceUTest {
    @Autowired
    TaskService taskService;

    @MockBean
    LabelService labelService;
    @MockBean
    TaskDatabaseRepository taskDatabaseRepository;


    @Test
    @DisplayName("it saves a task to the repo")
    void create() {
        // Given
        Set<Label> labels = new HashSet<>();
        labels.add(new Label("test_label1"));
        labels.add(new Label("test_label2"));

        Task task = new Task("Test task", false, true, 1, labels);
        when(taskDatabaseRepository.save(task)).thenReturn(task);

        // When
        Task createdTask = taskService.create(task);

        // Then
        verify(taskDatabaseRepository, times(1)).save(task);
        Assertions.assertEquals(task, createdTask);
    }


    @Test
    @DisplayName("it returns the task with matching id")
    void findById() {
        // Given
        Set<Label> labels = new HashSet<>();
        labels.add(new Label("test_label1"));
        labels.add(new Label("test_label2"));

        Task task = new Task("Test task", false, true, 1, labels);
        long taskId = 1;
        when(taskDatabaseRepository.findById(taskId)).thenReturn(Optional.of(task));

        // When
        Optional<Task> foundTask = taskService.findById(taskId);

        // Then
        verify(taskDatabaseRepository, times(1)).findById(taskId);
        Assertions.assertTrue(foundTask.isPresent());
        Assertions.assertEquals(foundTask.get(), task);
    }

    @Test
    @DisplayName("it finds all the tasks in the repo")
    void findAll() {
        // Given
        Set<Label> labels = new HashSet<>();
        labels.add(new Label("test_label1"));
        labels.add(new Label("test_label2"));

        Task task1 = new Task("Test task 1", false, true, 1, labels);
        Task task2 = new Task("Test task 2", false, false, 2, labels);

        when(taskDatabaseRepository.findAll()).thenReturn(Arrays.asList(task1, task2));

        // When
        List<Task> allTasks = taskService.findAll();

        // Then
        Assertions.assertArrayEquals(new Task[]{ task1, task2}, allTasks.toArray());
    }

    @Test
    @DisplayName("it finds all tasks by priority")
    void findByPriority() {
        // Given
        Set<Label> labels = new HashSet<>();
        labels.add(new Label("test_label1"));
        labels.add(new Label("test_label2"));

        Task task1 = new Task("Test task 1", false, true, 1, labels);
        Task task2 = new Task("Test task 2", false, false, 2, labels);
        Task task3 = new Task("Test task 3", false, true, 3, labels);


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
        Set<Label> labels = new HashSet<>();
        labels.add(new Label("test_label1"));
        labels.add(new Label("test_label2"));

        Task task1 = new Task("Test task 1", false, true, 1, labels);
        Task task2 = new Task("Test task 2", false, false, 2, labels);
        Task task3 = new Task("Test task 3", true, true, 3, labels);

        when(taskDatabaseRepository.findAll()).thenReturn(Arrays.asList(task1, task2, task3));

        // When
        List<Task> tasksInProgress = taskService.findByInProgress();

        // Then
        Assertions.assertArrayEquals(new Task[]{task1, task3}, tasksInProgress.toArray());
    }

    @Test
    @DisplayName("it finds all tasks by incomplete")
    void findByIncomplete() {
        // Given
        Set<Label> labels = new HashSet<>();
        labels.add(new Label("test_label1"));
        labels.add(new Label("test_label2"));

        Task task1 = new Task("Test task 1", false, true, 1, labels);
        Task task2 = new Task("Test task 2", false, false, 2, labels);
        Task task3 = new Task("Test task 3", true, true, 3, labels);


        when(taskDatabaseRepository.findAll()).thenReturn(Arrays.asList(task1, task2, task3));

        // When
        List<Task> incompleteTasks = taskService.findByIncomplete();

        // Then
        Assertions.assertArrayEquals(new Task[]{task1, task2}, incompleteTasks.toArray());
    }
}