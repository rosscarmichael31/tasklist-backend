package com.thg.accelerator.tasklist.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TASKS")
public class Task {
    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private Long id;
    private String description;
    private boolean complete;
    private boolean inProgress;
    private int priority;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "TASK_LABELS",
            joinColumns = @JoinColumn(name = "task_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "label_id", referencedColumnName = "id")
    )
    private Set<Label> labels = new HashSet<>();

    public Task(String description, boolean complete, boolean inProgress, int priority) {
        if (description == null || description.trim().equals("")) {
            throw new IllegalArgumentException("Description must not be empty");
        }
        if (priority > 3 || priority < 0) {
            throw new IllegalArgumentException("Priority must be between 0 and 3");
        }
        this.description = description;
        this.complete = complete;
        this.inProgress = inProgress;
        this.priority = priority;
    }

    public Task(String description, boolean complete, boolean inProgress, int priority, Set<Label> labels) {
        if (description == null || description.trim().equals("")) {
            throw new IllegalArgumentException("Description must not be empty");
        }
        if (priority > 3 || priority < 0) {
            throw new IllegalArgumentException("Priority must be between 0 and 3");
        }
        this.description = description;
        this.complete = complete;
        this.inProgress = inProgress;
        this.priority = priority;
        this.labels = labels;
    }
}