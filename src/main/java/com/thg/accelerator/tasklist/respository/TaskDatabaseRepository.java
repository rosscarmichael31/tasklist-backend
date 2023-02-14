package com.thg.accelerator.tasklist.respository;

import com.thg.accelerator.tasklist.model.Task;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskDatabaseRepository extends CrudRepository<Task, Long> {
}
