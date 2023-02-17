package com.thg.accelerator.tasklist.controller;


import com.thg.accelerator.tasklist.model.Task;
import com.thg.accelerator.tasklist.service.LabelService;
import com.thg.accelerator.tasklist.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@CrossOrigin
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;
    private final LabelService labelService;

    public TaskController(TaskService taskService, LabelService labelService) {
        this.taskService = taskService;
        this.labelService = labelService;
    }

    @PostMapping
    public ResponseEntity<Task> create(@RequestBody Task task) {
        try {
            log.info("POST: " + task);
            Task createdTask = taskService.create(task);
            return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Error creating task: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> findById(@PathVariable long id) {
        log.info("GET: " + id);

        try {
            Optional<Task> optionalTask = taskService.findById(id);
            return optionalTask.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            log.error("An error occurred while fetching task with id " + id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping
    public List<Task> findAll(@RequestParam(required = false) String sortBy) {
        log.info("GET: " + sortBy);
        try {
            return switch (Query.fromString(sortBy)) {
                case PRIORITY -> taskService.findByPriority();
                case IN_PROGRESS -> taskService.findByInProgress();
                case INCOMPLETE -> taskService.findByIncomplete();
            };
        } catch (IllegalArgumentException e) {
            return taskService.findAll();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@RequestBody Task task, @PathVariable long id) {
        log.info("PUT: " + task);
        try {
            Optional<Task> optionalTask = taskService.findById(id);
            if (optionalTask.isPresent()) {
                Task updatedTask = new Task(
                        id,
                        task.getDescription(),
                        task.isComplete(),
                        task.isInProgress(),
                        task.getPriority(),
                        task.getLabels()
                );

                taskService.delete(id);
                taskService.create(updatedTask);

                return new ResponseEntity<>(updatedTask, HttpStatus.OK);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable long id) {
        log.info("DELETE: " + id);
        try {
            Optional<Task> task = taskService.findById(id);
            if(task.isPresent()) {
                task.get().getLabels().forEach(label -> labelService.delete(label.getId()));
            } else {
                throw new IllegalArgumentException("Task could not be found");
            }
            taskService.delete(id);

            return new ResponseEntity<>("Task successfully deleted!", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return new ResponseEntity<>("Task could not be deleted", HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }
}
