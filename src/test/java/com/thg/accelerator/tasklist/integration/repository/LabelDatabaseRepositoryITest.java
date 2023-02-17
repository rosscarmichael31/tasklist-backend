package com.thg.accelerator.tasklist.integration.repository;

import com.thg.accelerator.tasklist.model.Label;
import com.thg.accelerator.tasklist.model.Task;
import com.thg.accelerator.tasklist.respository.LabelDatabaseRepository;
import com.thg.accelerator.tasklist.respository.TaskDatabaseRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

@DataJpaTest
public class LabelDatabaseRepositoryITest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private LabelDatabaseRepository labelDatabaseRepository;

    @Test
    @DisplayName("it adds to database and returns a label entity")
    public void databasePersistence() {
        // given
        Label label = new Label("test_label");
        entityManager.persist(label);
        entityManager.flush();

        // when
        Optional<Label> found = labelDatabaseRepository.findById(label.getId());

        // then
        Assertions.assertTrue(found.isPresent());
        Assertions.assertEquals(found.get().getName(), label.getName());

    }

}