package com.thg.accelerator.tasklist.service;

import com.thg.accelerator.tasklist.model.Task;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public interface TaskServiceInterface {

    Task create(Task task);

    Optional<Task> findById(long id);

    List<Task> findAll();

    List<Task> findByPriority();

    List<Task> findByInProgress();

    List<Task> findByIncomplete();

    Task update(Task task, long id);

    void delete(long id);

}
