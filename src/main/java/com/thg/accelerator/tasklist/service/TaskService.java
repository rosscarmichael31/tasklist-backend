package com.thg.accelerator.tasklist.service;

import com.thg.accelerator.tasklist.model.Task;
import com.thg.accelerator.tasklist.respository.TaskDatabaseRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
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
        List<Task> tasks = taskDatabaseRepository.findAll();
        log.info("SERVICE.taskRepo.findAll(): {}", tasks);
        return tasks;
    }

    @Override
    public List<Task> findByPriority() {
        return taskDatabaseRepository.findAll().stream()
                .sorted(Comparator.comparing(Task::getPriority, Comparator.nullsLast(Comparator.reverseOrder())))
                .collect(Collectors.toList());
    }

    @Override
    public List<Task> findByInProgress() {
        return taskDatabaseRepository.findAll().stream()
                .filter(Task::isInProgress)
                .collect(Collectors.toList());
    }

    @Override
    public List<Task> findByIncomplete() {
        return taskDatabaseRepository.findAll().stream()
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
