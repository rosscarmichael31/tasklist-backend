package com.thg.accelerator.tasklist.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@Entity
@Table(name = "LABELS")
public class Label {
    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private Long id;
    private String name;

    @ManyToMany(mappedBy = "labels", fetch = FetchType.EAGER)
    private Set<Task> tasks;

    public Label(String name) {
        this.name = name;
    }

    public Label(long id, String name) {
        this.id = id;
        this.name = name;
    }
}


