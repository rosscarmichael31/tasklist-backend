package com.thg.accelerator.tasklist.controller;

public enum Query {
    PRIORITY("priority"),
    IN_PROGRESS("in-progress"),
    INCOMPLETE("incomplete");

    private final String label;

    Query(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
