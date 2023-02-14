package com.thg.accelerator.tasklist.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TaskDto {
    private String description;
    private boolean complete;
    private boolean inProgress;
    private int priority;
    private List<String> labelNames = new ArrayList<>();

    public TaskDto() {}

    public TaskDto(Task task) {
        this.description = task.getDescription();
        this.complete = task.isComplete();
        this.inProgress = task.isInProgress();
        this.priority = task.getPriority();
        this.labelNames = task.getLabels().stream().map(Label::getName).collect(Collectors.toList());
    }

    public TaskDto(String description, boolean complete, boolean inProgress, int priority, List<String> labelNames) {
        this.description = description;
        this.complete = complete;
        this.inProgress = inProgress;
        this.priority = priority;
        this.labelNames = labelNames;
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
    public String toString() {
        return "com.thg.accelerator.tasklist.model.TaskDto{" +
                "description='" + description + '\'' +
                ", complete=" + complete +
                ", inProgress=" + inProgress +
                ", priority=" + priority +
                ", labels=" + labelNames +
                '}';
    }
}