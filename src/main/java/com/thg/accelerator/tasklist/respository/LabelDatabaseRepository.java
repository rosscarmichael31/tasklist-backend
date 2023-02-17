package com.thg.accelerator.tasklist.respository;

import com.thg.accelerator.tasklist.model.Label;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LabelDatabaseRepository extends JpaRepository<Label, Long> {
    Optional<Label> findByName(String name);
}
