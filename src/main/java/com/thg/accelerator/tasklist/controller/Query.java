package com.thg.accelerator.tasklist.controller;

public enum Query {
    PRIORITY("priority"),
    IN_PROGRESS("in-progress"),
    INCOMPLETE("incomplete");

    private final String label;

    Query(String label) {
        this.label = label;
    }

    public static Query fromString(String text) {
        for (Query q : Query.values()) {
            if (q.label.equalsIgnoreCase(text)) {
                return q;
            }
        }
        throw new IllegalArgumentException("illegal query string");
    }

    public String getLabel() {
        return label;
    }
}
