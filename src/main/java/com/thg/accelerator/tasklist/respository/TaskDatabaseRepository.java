package com.thg.accelerator.tasklist.respository;

import com.thg.accelerator.tasklist.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskDatabaseRepository extends JpaRepository<Task, Long> {
}
