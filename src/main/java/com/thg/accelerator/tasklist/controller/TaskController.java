package com.thg.accelerator.tasklist.controller;

import com.thg.accelerator.tasklist.model.Label;
import com.thg.accelerator.tasklist.model.Task;
import com.thg.accelerator.tasklist.model.TaskDto;
import com.thg.accelerator.tasklist.service.LabelService;
import com.thg.accelerator.tasklist.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.thg.accelerator.tasklist.controller.Query.*;

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
    public ResponseEntity<TaskDto> create(@RequestBody TaskDto taskDto) {
        Task task = taskService.create(
                new Task(taskDto.getDescription(), taskDto.isComplete(), taskDto.isInProgress(), taskDto.getPriority())
        );
        System.out.println(taskDto);
        if (!taskDto.getLabelNames().isEmpty()) {
            Set<Label> labels = taskDto.getLabelNames()
                    .stream()
                    .map(labelService::findOrCreateLabel)
                    .collect(Collectors.toSet());
            task.setLabels(labels);
            taskService.update(task, task.getId());
        }

        TaskDto response = new TaskDto(task);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> findById(@PathVariable long id) {
        return taskService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<Task> findAll(@RequestParam(required = false) String sortBy) {

        if (Objects.equals(sortBy, PRIORITY.getLabel())) {
            return taskService.findByPriority();
        }

        if (Objects.equals(sortBy, IN_PROGRESS.getLabel())) {
            return taskService.findByInProgress();
        }

        if (Objects.equals(sortBy, INCOMPLETE.getLabel())) {
            return taskService.findByIncomplete();
        }
        return taskService.findAll();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> update(@RequestBody Task task, @PathVariable long id) {
        return taskService.findById(id)
                .map(savedTask -> {
                    savedTask.setDescription(task.getDescription());
                    savedTask.setComplete(task.isComplete());
                    savedTask.setInProgress(task.isInProgress());
                    savedTask.setPriority(task.getPriority());

                    Task updatedTask = taskService.update(task, id);
                    return new ResponseEntity<>(updatedTask, HttpStatus.OK);

                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable long id) {
        taskService.delete(id);
        return new ResponseEntity<>("Task successfully deleted!", HttpStatus.OK);
    }


}
