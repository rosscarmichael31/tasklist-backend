package com.thg.accelerator.tasklist.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskDTO {
    private Long id;
    private String description;
    private boolean complete;
    private boolean inProgress;
    private int priority;
    private Set<Label> labels;
}