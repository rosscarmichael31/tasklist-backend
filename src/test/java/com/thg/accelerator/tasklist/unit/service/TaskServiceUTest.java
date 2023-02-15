package com.thg.accelerator.tasklist.unit.service;

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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;


@SpringBootTest
@AutoConfigureMockMvc
class TaskServiceUTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    TaskService taskService;
    @MockBean
    LabelService labelService;

    @Autowired
    TaskMapper taskmapper;

    @Autowired
    TaskDTOMapper taskDTOMapper = new TaskDTOMapper();
    @MockBean
    TaskDatabaseRepository taskDatabaseRepository;


    @Test
    @DisplayName("it saves a task to the repo")
    void create() {

        // Given
        Task task = new Task("Test task", false, true, 1);
        when(taskDatabaseRepository.save(task)).thenReturn(task);
        TaskDTO taskDTO = taskDTOMapper.apply(task);

        // When
        TaskDTO createdTask = taskService.create(taskDTO);

        // Then
        verify(taskDatabaseRepository, times(1)).save(task);
        Assertions.assertEquals(taskDTO, createdTask);
    }


    @Test
    @DisplayName("it returns the task with matching id")
    void findById() {
        // Given
        Task task = new Task("Test task", false, true, 1);
        long taskId = 1;
        when(taskDatabaseRepository.findById(taskId)).thenReturn(Optional.of(task));
        TaskDTO taskDTO = taskDTOMapper.apply(task);

        // When
        Optional<TaskDTO> foundTask = taskService.findById(taskId);

        // Then
        Assertions.assertTrue(foundTask.isPresent());
        Assertions.assertEquals(foundTask.get(), taskDTO);


    }

    @Test
    @DisplayName("it finds all the tasks in the repo")
    void findAll() {
        // Given
        Task task1 = new Task("Test task 1", false, true, 1);
        Task task2 = new Task("Test task 2", false, false, 2);

        TaskDTO task1DTO = taskDTOMapper.apply(task1);
        TaskDTO task2DTO = taskDTOMapper.apply(task2);

        when(taskDatabaseRepository.findAll()).thenReturn(Arrays.asList(task1, task2));

        // When
        List<TaskDTO> allTasks = taskService.findAll();

        // Then
        Assertions.assertArrayEquals(new TaskDTO[]{ task1DTO, task2DTO}, allTasks.toArray());
    }

    @Test
    @DisplayName("it finds all tasks by priority")
    void findByPriority() {
        // Given
        Task task1 = new Task("Test task 1", false, true, 1);
        Task task2 = new Task("Test task 2", false, false, 2);
        Task task3 = new Task("Test task 3", false, true, 3);

        TaskDTO task1DTO = taskDTOMapper.apply(task1);
        TaskDTO task2DTO = taskDTOMapper.apply(task2);
        TaskDTO task3DTO = taskDTOMapper.apply(task3);

        when(taskDatabaseRepository.findAll()).thenReturn(Arrays.asList(task1, task2, task3));

        // When
        List<TaskDTO> tasksByPriority = taskService.findByPriority();

        // Then
        Assertions.assertArrayEquals(new TaskDTO[]{task3DTO, task2DTO, task1DTO}, tasksByPriority.toArray());
    }

    @Test
    @DisplayName("it finds all tasks in progress")
    void findByInProgress() {
        // Given
        Task task1 = new Task("Test task 1", false, true, 1);
        Task task2 = new Task("Test task 2", false, false, 2);
        Task task3 = new Task("Test task 3", true, true, 3);

        TaskDTO task1DTO = taskDTOMapper.apply(task1);
        TaskDTO task3DTO = taskDTOMapper.apply(task3);

        when(taskDatabaseRepository.findAll()).thenReturn(Arrays.asList(task1, task2, task3));

        // When
        List<TaskDTO> tasksInProgress = taskService.findByInProgress();

        // Then
        Assertions.assertArrayEquals(new TaskDTO[]{task1DTO, task3DTO}, tasksInProgress.toArray());
    }

    @Test
    void findByIncomplete() {
        // Given
        Task task1 = new Task("Test task 1", false, true, 1);
        Task task2 = new Task("Test task 2", false, false, 2);
        Task task3 = new Task("Test task 3", true, true, 3);

        TaskDTO task1DTO = taskDTOMapper.apply(task1);
        TaskDTO task2DTO = taskDTOMapper.apply(task2);

        when(taskDatabaseRepository.findAll()).thenReturn(Arrays.asList(task1, task2, task3));

        // When
        List<TaskDTO> incompleteTasks = taskService.findByIncomplete();

        // Then
        Assertions.assertArrayEquals(new TaskDTO[]{task1DTO, task2DTO}, incompleteTasks.toArray());
    }
}