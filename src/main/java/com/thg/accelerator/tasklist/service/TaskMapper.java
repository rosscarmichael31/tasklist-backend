package com.thg.accelerator.tasklist.service;

import com.thg.accelerator.tasklist.model.Label;
import com.thg.accelerator.tasklist.model.Task;
import com.thg.accelerator.tasklist.model.TaskDTO;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class TaskMapper implements Function<TaskDTO, Task> {
    private final LabelService labelService;

    public TaskMapper(LabelService labelService) {
        this.labelService = labelService;
    }

    @Override
    public Task apply(TaskDTO taskDTO) {
        Task task = new Task(
                taskDTO.getDescription(),
                taskDTO.isComplete(),
                taskDTO.isInProgress(),
                taskDTO.getPriority(),
                taskDTO.getLabels()
        );


        return task;
    }
}
