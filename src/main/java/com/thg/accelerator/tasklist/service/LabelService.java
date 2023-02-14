package com.thg.accelerator.tasklist.service;

import com.thg.accelerator.tasklist.model.Label;
import com.thg.accelerator.tasklist.respository.LabelDatabaseRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LabelService implements LabelServiceInterface {

    private final LabelDatabaseRepository labelDatabaseRepository;

    public LabelService(LabelDatabaseRepository labelDatabaseRepository) {
        this.labelDatabaseRepository = labelDatabaseRepository;
    }

    @Override
    public Label create(Label label) {
        return labelDatabaseRepository.save(label);
    }

    @Override
    public Optional<Label> findById(long id) {
        return labelDatabaseRepository.findById(id);
    }

    @Override
    public Label findOrCreateLabel(String name) {
        Optional<Label> optionalLabel = labelDatabaseRepository.findByName(name);
        if (optionalLabel.isPresent()) {
            return optionalLabel.get();
        } else {
            Label newLabel = new Label();
            newLabel.setName(name);
            return labelDatabaseRepository.save(newLabel);
        }
    }

    @Override
    public Label update(long id, Label label) {
        return labelDatabaseRepository.save(label);
    }

    @Override
    public void delete(long id) {
        labelDatabaseRepository.deleteById(id);
    }
}
