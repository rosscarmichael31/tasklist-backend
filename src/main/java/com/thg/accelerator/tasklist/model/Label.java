package com.thg.accelerator.tasklist.model;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "LABELS")
public class Label {
    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private long id;
    private String name;

    @ManyToMany(mappedBy = "labels")
    private Set<Task> tasks = new HashSet<>();

    public Label() {
    }

    public Label(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Task> getTasks() {
        return tasks;
    }

    public void setTasks(Set<Task> tasks) {
        this.tasks = tasks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Label label = (Label) o;
        return id == label.id && Objects.equals(name, label.name) && Objects.equals(tasks, label.tasks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, tasks);
    }

    @Override
    public String toString() {
        return "com.thg.accelerator.tasklist.model.Label{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", tasks=" + tasks +
                '}';
    }
}


