package com.thg.accelerator.tasklist.model;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "TASKS")
public class Task {
    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private long id;
    private String description;
    private boolean complete;
    private boolean inProgress;
    private int priority;
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "TASK_LABELS",
            joinColumns = @JoinColumn(name = "task_id"),
            inverseJoinColumns = @JoinColumn(name = "label_id")
    )
    private Set<Label> labels = new HashSet<>();
    public Task(){}

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
    public Task(long id, String description, boolean complete, boolean inProgress, int priority) {
        this.id = id;
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
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public boolean isInProgress() {
        return inProgress;
    }

    public void setInProgress(boolean inProgress) {
        this.inProgress = inProgress;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public Set<Label> getLabels() {
        return labels;
    }

    public void setLabels(Set<Label> labels) {
        this.labels = labels;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && complete == task.complete && inProgress == task.inProgress && priority == task.priority && Objects.equals(description, task.description) && Objects.equals(labels, task.labels);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, complete, inProgress, priority, labels);
    }

    @Override
    public String toString() {
        return "com.thg.accelerator.tasklist.model.Task{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", complete=" + complete +
                ", inProgress=" + inProgress +
                ", priority=" + priority +
                ", labels=" + labels +
                '}';
    }
}