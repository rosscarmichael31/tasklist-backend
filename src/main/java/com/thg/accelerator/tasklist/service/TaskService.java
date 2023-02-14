package com.thg.accelerator.tasklist.service;

import com.thg.accelerator.tasklist.model.Label;
import com.thg.accelerator.tasklist.model.Task;
import com.thg.accelerator.tasklist.respository.TaskDatabaseRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class TaskService implements TaskServiceInterface {

    private final TaskDatabaseRepository taskDatabaseRepository;

    public TaskService(TaskDatabaseRepository taskDatabaseRepository) {
        this.taskDatabaseRepository = taskDatabaseRepository;
    }

    @Override
    public Task create(Task task) {
        return taskDatabaseRepository.save(task);
    }

    @Override
    public Optional<Task> findById(long id) {
        return taskDatabaseRepository.findById(id);
    }

    @Override
    public List<Task> findAll() {
        return (List<Task>) taskDatabaseRepository.findAll();
    }

    @Override
    public List<Task> findByPriority() {
        return StreamSupport.stream(taskDatabaseRepository.findAll().spliterator(), false)
                .sorted(Comparator.comparing(Task::getPriority, Comparator.nullsLast(Comparator.reverseOrder())))
                .collect(Collectors.toList());
    }

    @Override
    public List<Task> findByInProgress() {
        return StreamSupport.stream(taskDatabaseRepository.findAll().spliterator(), false)
                .filter(Task::isInProgress)
                .collect(Collectors.toList());
    }

    @Override
    public List<Task> findByIncomplete() {
        return StreamSupport.stream(taskDatabaseRepository.findAll().spliterator(), false)
                .filter(task -> !task.isComplete())
                .collect(Collectors.toList());
    }

    @Override
    public Task update(Task task, long id) {
        return taskDatabaseRepository.save(task);
    }

    @Override
    public void delete(long id) {
        taskDatabaseRepository.deleteById(id);
    }
}
