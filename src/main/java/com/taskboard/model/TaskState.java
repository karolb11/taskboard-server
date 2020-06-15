package com.taskboard.model;

import org.hibernate.annotations.NaturalId;

import javax.persistence.*;

@Entity
@Table(name = "task_states")
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TaskStateName getName() {
        return name;
    }

    public void setName(TaskStateName name) {
        this.name = name;
    }
}
