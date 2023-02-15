package com.thg.accelerator.tasklist.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskDTO {
    private Long id;
    private String description;
    private boolean complete;
    private boolean inProgress;
    private int priority;
    private List<String> labelNames = new ArrayList<>();
}