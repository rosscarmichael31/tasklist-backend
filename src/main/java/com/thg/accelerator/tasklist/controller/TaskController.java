package com.thg.accelerator.tasklist.controller;

import com.thg.accelerator.tasklist.model.Label;
import com.thg.accelerator.tasklist.model.Task;
import com.thg.accelerator.tasklist.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static com.thg.accelerator.tasklist.controller.Query.*;

@RestController
@CrossOrigin
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Task create(@Valid @RequestBody Task task) {
        return taskService.create(task);
    }

    //TODO: NEW
    @PostMapping("/{id}/labels")
    public ResponseEntity<Task> addLabels(@PathVariable("id") long id, @RequestBody Set<Label> labels) {
        Optional<Task> task = taskService.findById(id);
        if (task.isPresent()) {
            Task updatedTask = task.get();
            updatedTask.setLabels(labels);
            taskService.update(updatedTask, id);
            return ResponseEntity.ok(updatedTask);
        } else {
            return ResponseEntity.notFound().build();
        }
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
