package com.thg.accelerator.tasklist;

import com.thg.accelerator.tasklist.model.Task;
import com.thg.accelerator.tasklist.respository.TaskDatabaseRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class TasklistApplication implements CommandLineRunner {

    private final TaskDatabaseRepository taskDatabaseRepository;

    public TasklistApplication(TaskDatabaseRepository taskDatabaseRepository) {
        this.taskDatabaseRepository = taskDatabaseRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(TasklistApplication.class, args);
    }

    @Override
    public void run(String... args) {
        log.info("reading data");
        for (Task task : taskDatabaseRepository.findAll()) {
            log.info("ID: {}     Description: {}     Complete: {}    In progress: {}     Priority: {}       Labels: {}",
                    task.getId(), task.getDescription(), task.isComplete(), task.isInProgress(), task.getPriority(), task.getLabels());

        }
    }
}