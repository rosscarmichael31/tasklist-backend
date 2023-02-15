package com.thg.accelerator.tasklist.service;

import com.thg.accelerator.tasklist.model.Label;
import com.thg.accelerator.tasklist.model.Task;
import com.thg.accelerator.tasklist.model.TaskDTO;
import org.springframework.stereotype.Service;

import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class TaskDTOMapper implements Function<Task, TaskDTO> {
    @Override
    public TaskDTO apply(Task task) {
        return new TaskDTO(
                task.getId(),
                task.getDescription(),
                task.isComplete(),
                task.isInProgress(),
                task.getPriority(),
                task.getLabels()
                        .stream()
                        .map(Label::getName)
                        .collect(Collectors.toList()));
    }
}
