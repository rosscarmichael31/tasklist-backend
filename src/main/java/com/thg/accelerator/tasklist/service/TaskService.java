package com.thg.accelerator.tasklist.service;

import com.thg.accelerator.tasklist.model.Task;
import com.thg.accelerator.tasklist.model.TaskDTO;
import com.thg.accelerator.tasklist.respository.TaskDatabaseRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class TaskService implements TaskServiceInterface {

    private final TaskDatabaseRepository taskDatabaseRepository;
    private final TaskDTOMapper taskDTOMapper;
    private final TaskMapper taskMapper;


    public TaskService(TaskDatabaseRepository taskDatabaseRepository, TaskDTOMapper taskDTOMapper, TaskMapper taskMapper) {
        this.taskDatabaseRepository = taskDatabaseRepository;
        this.taskDTOMapper = taskDTOMapper;
        this.taskMapper = taskMapper;
    }

    @Override
    public TaskDTO create(TaskDTO taskDTO) {
        return taskDTOMapper.apply(
                taskDatabaseRepository.save(taskMapper.apply(taskDTO)));
    }

    @Override
    public Optional<TaskDTO> findById(long id) {
        return taskDatabaseRepository.findById(id)
                .stream()
                .map(taskDTOMapper)
                .findFirst();
    }

    @Override
    public List<TaskDTO> findAll() {
        List<Task> tasks = (List<Task>) taskDatabaseRepository.findAll();
        return tasks
                .stream()
                .map(taskDTOMapper)
                .collect(Collectors.toList());

    }

    @Override
    public List<TaskDTO> findByPriority() {
        return StreamSupport.stream(taskDatabaseRepository.findAll().spliterator(), false)
                .sorted(Comparator.comparing(Task::getPriority, Comparator.nullsLast(Comparator.reverseOrder())))
                .map(taskDTOMapper)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskDTO> findByInProgress() {
        return StreamSupport.stream(taskDatabaseRepository.findAll().spliterator(), false)
                .filter(Task::isInProgress)
                .map(taskDTOMapper)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskDTO> findByIncomplete() {
        return StreamSupport.stream(taskDatabaseRepository.findAll().spliterator(), false)
                .filter(task -> !task.isComplete())
                .map(taskDTOMapper)
                .collect(Collectors.toList());
    }

    @Override
    public TaskDTO update(Task task, long id) {
        return taskDTOMapper.apply(taskDatabaseRepository.save(task));

    }

    @Override
    public void delete(long id) {
        taskDatabaseRepository.deleteById(id);
    }
}
