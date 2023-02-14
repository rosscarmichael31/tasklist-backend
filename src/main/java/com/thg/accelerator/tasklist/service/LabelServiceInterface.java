package com.thg.accelerator.tasklist.service;

import com.thg.accelerator.tasklist.model.Label;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface LabelServiceInterface {
    Label create(Label label);
    Optional<Label> findById(long id);
    Label findOrCreateLabel(String name);
    Label update(long id, Label label);
    void delete(long id);
}
