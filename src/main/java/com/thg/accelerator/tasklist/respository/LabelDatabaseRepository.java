package com.thg.accelerator.tasklist.respository;

import com.thg.accelerator.tasklist.model.Label;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LabelDatabaseRepository extends CrudRepository<Label, Long> {
    Optional<Label> findByName(String name);
}
