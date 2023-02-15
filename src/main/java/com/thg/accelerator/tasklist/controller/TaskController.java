package com.thg.accelerator.tasklist.controller;


import com.thg.accelerator.tasklist.model.TaskDTO;
import com.thg.accelerator.tasklist.service.LabelService;
import com.thg.accelerator.tasklist.service.TaskDTOMapper;
import com.thg.accelerator.tasklist.service.TaskMapper;
import com.thg.accelerator.tasklist.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;
    private final LabelService labelService;
    private final TaskDTOMapper taskDTOMapper;
    private final TaskMapper taskMapper;

    public TaskController(TaskService taskService, LabelService labelService, TaskDTOMapper taskDTOMapper, TaskMapper taskMapper) {
        this.taskService = taskService;
        this.labelService = labelService;
        this.taskDTOMapper = taskDTOMapper;
        this.taskMapper = taskMapper;
    }

    @PostMapping
    public ResponseEntity<TaskDTO> create(@RequestBody TaskDTO taskDTO) {
        taskService.create(taskDTO);
        return new ResponseEntity<>(taskDTO, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> findById(@PathVariable long id) {
        return taskService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<TaskDTO> findAll(@RequestParam(required = false) String sortBy) {
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
        taskService.delete(id);
        return new ResponseEntity<>("Task successfully deleted!", HttpStatus.OK);
    }
}
