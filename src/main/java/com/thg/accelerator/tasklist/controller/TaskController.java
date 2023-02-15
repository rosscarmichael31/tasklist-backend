package com.thg.accelerator.tasklist.controller;


import com.thg.accelerator.tasklist.model.TaskDTO;
import com.thg.accelerator.tasklist.service.TaskMapper;
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
    private final TaskMapper taskMapper;

    public TaskController(TaskService taskService, TaskMapper taskMapper) {
        this.taskService = taskService;
        this.taskMapper = taskMapper;
    }

    @PostMapping
    public ResponseEntity<TaskDTO> create(@RequestBody TaskDTO taskDTO) {
        log.info("POST: " + taskDTO);
        return new ResponseEntity<>(taskService.create(taskDTO), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> findById(@PathVariable long id) {
        log.info("GET: " + id);
        return taskService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<TaskDTO> findAll(@RequestParam(required = false) String sortBy) {
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
    public ResponseEntity<TaskDTO> update(@RequestBody TaskDTO taskDTO, @PathVariable long id) {
        log.info("PUT: " + taskDTO );
        Optional<TaskDTO> optionalTask = taskService.findById(id);
        if (optionalTask.isPresent()) {
            TaskDTO existingTaskDTO = optionalTask.get();
            existingTaskDTO.setDescription(taskDTO.getDescription());
            existingTaskDTO.setComplete(taskDTO.isComplete());
            existingTaskDTO.setInProgress(taskDTO.isInProgress());
            existingTaskDTO.setPriority(taskDTO.getPriority());
            existingTaskDTO.setLabelNames(taskDTO.getLabelNames());

            TaskDTO updatedTask = taskService.update(taskMapper.apply(taskDTO), id);
            return new ResponseEntity<>(updatedTask, HttpStatus.OK);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable long id) {
        log.info("DELETE: " + id);
        taskService.delete(id);
        return new ResponseEntity<>("Task successfully deleted!", HttpStatus.OK);
    }
}
