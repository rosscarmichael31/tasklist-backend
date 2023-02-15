package com.thg.accelerator.tasklist.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


public class TaskDTO {
    private long id;
    private String description;
    private boolean complete;
    private boolean inProgress;
    private int priority;
    private List<String> labelNames = new ArrayList<>();

    public TaskDTO() {
    }

    public TaskDTO(Task task) {
        this.id = task.getId();
        this.description = task.getDescription();
        this.complete = task.isComplete();
        this.inProgress = task.isInProgress();
        this.priority = task.getPriority();
        this.labelNames = task.getLabels().stream().map(Label::getName).collect(Collectors.toList());
    }

    public TaskDTO(long id, String description, boolean complete, boolean inProgress, int priority, List<String> labelNames) {
        this.id = id;
        this.description = description;
        this.complete = complete;
        this.inProgress = inProgress;
        this.priority = priority;
        this.labelNames = labelNames;
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

    public List<String> getLabelNames() {
        return labelNames;
    }

    public void setLabelNames(List<String> labelNames) {
        this.labelNames = labelNames;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskDTO taskDTO = (TaskDTO) o;
        return id == taskDTO.id && complete == taskDTO.complete && inProgress == taskDTO.inProgress && priority == taskDTO.priority && Objects.equals(description, taskDTO.description) && Objects.equals(labelNames, taskDTO.labelNames);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, complete, inProgress, priority, labelNames);
    }

    @Override
    public String toString() {
        return "com.thg.accelerator.tasklist.model.TaskDTO{" +
                "description='" + description + '\'' +
                ", complete=" + complete +
                ", inProgress=" + inProgress +
                ", priority=" + priority +
                ", labels=" + labelNames +
                '}';
    }
}