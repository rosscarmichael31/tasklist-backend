package com.thg.accelerator.tasklist;

import com.thg.accelerator.tasklist.model.Task;
import com.thg.accelerator.tasklist.respository.TaskDatabaseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TasklistApplication implements CommandLineRunner {

    public static final Logger log = LoggerFactory.getLogger(TasklistApplication.class);

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
            log.info("ID: {}     Description: {}     Complete: {}    In progress: {}     Priority: {}",
                    task.getId(), task.getDescription(), task.isComplete(), task.isInProgress(), task.getPriority());

        }
    }

}