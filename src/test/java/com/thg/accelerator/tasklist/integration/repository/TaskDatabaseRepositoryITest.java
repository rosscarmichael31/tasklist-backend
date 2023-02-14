package com.thg.accelerator.tasklist.integration.repository;

import com.thg.accelerator.tasklist.model.Task;
import com.thg.accelerator.tasklist.respository.TaskDatabaseRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

@DataJpaTest
public class TaskDatabaseRepositoryITest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TaskDatabaseRepository taskDatabaseRepository;

    @Test
    public void whenFindByName_thenReturnEmployee() {
        // given
        Task task = new Task("test_task", false, false, 1);
        entityManager.persist(task);
        entityManager.flush();

        // when
        Optional<Task> found = taskDatabaseRepository.findById(task.getId());

        // then
        Assertions.assertEquals(found.get().getDescription(), task.getDescription());

    }

}
