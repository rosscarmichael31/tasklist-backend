package com.thg.accelerator.tasklist.service;

import com.thg.accelerator.tasklist.model.Task;
import com.thg.accelerator.tasklist.model.TaskDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public interface TaskServiceInterface {
    TaskDTO create(TaskDTO taskDTO);
    Optional<TaskDTO> findById(long id);
    List<TaskDTO> findAll();
    List<TaskDTO> findByPriority();
    List<TaskDTO> findByInProgress();
    List<TaskDTO> findByIncomplete();
    TaskDTO update(Task task, long id);
    void delete(long id);

}
