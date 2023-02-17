package com.thg.accelerator.tasklist.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "LABELS")
public class Label {
    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private Long id;
    private String name;
    public Label(String name) {
        this.name = name;
    }

    public Label(long id, String name) {
        this.id = id;
        this.name = name;
    }
}


