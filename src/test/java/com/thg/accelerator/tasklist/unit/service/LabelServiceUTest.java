package com.thg.accelerator.tasklist.unit.service;

import com.thg.accelerator.tasklist.model.Label;
import com.thg.accelerator.tasklist.respository.LabelDatabaseRepository;
import com.thg.accelerator.tasklist.service.LabelService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.mockito.Mockito.*;

@SpringBootTest
class LabelServiceUTest {

    @Autowired
    LabelService labelService;
    @MockBean
    LabelDatabaseRepository labelDatabaseRepository;

    @Test
    @DisplayName("it saves a label to the repo")
    void create() {
        // Given
        Label label = new Label(999, "Test_label");
        when(labelDatabaseRepository.save(label)).thenReturn(label);

        // When
        Label createdLabel = labelService.create(label);

        // Then
        verify(labelDatabaseRepository, times(1)).save(label);
        Assertions.assertEquals(label, createdLabel);
    }

    @Test
    @DisplayName("it returns the label with matching id")
    void findById() {
        // Given
        long testId = 999;
        Label label = new Label(testId, "Test_label");
        when(labelDatabaseRepository.findById(testId)).thenReturn(Optional.of(label));

        // When
        Optional<Label> foundLabel = labelService.findById(testId);

        // Then
        verify(labelDatabaseRepository, times(1)).findById(testId);
        Assertions.assertTrue(foundLabel.isPresent());
        Assertions.assertEquals(label, foundLabel.get());
    }

    @Test
    @DisplayName("it finds a label when present")
    void findOrCreateLabelFind() {
        // Given
        String testName = "test_label";
        Label label = new Label(999, testName);
        when(labelDatabaseRepository.findByName(testName)).thenReturn(Optional.of(label));

        // When
        Label foundLabel = labelService.findOrCreateLabel(testName);

        // Then
        verify(labelDatabaseRepository, times(1)).findByName(testName);
        Assertions.assertEquals(label, foundLabel);
    }

    @Test
    @DisplayName("it creates a label when not present")
    void findOrCreateLabelCreate() {
        // Given
        String testName = "test_label";
        Label label = new Label(999, testName);
        when(labelDatabaseRepository.findByName(testName)).thenReturn(Optional.empty());
        when(labelDatabaseRepository.save(any())).thenReturn(label);

        // When
        Label foundLabel = labelService.findOrCreateLabel(testName);

        // Then
        verify(labelDatabaseRepository, times(1)).findByName(testName);
        verify(labelDatabaseRepository, times(1)).save(any(Label.class));
        Assertions.assertEquals(label, foundLabel);
    }
}