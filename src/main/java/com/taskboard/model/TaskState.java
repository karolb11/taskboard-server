package com.taskboard.model;

import lombok.Data;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;

@Entity
@Table(name = "task_states")
@Data
public class TaskState {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @NaturalId
    @Column(length = 60)
    private TaskStateName name;

    public TaskState() {

    }

    public TaskState(TaskStateName name) {
        this.name = name;
    }

}
