package com.taskboard.model;

import org.hibernate.annotations.NaturalId;

import javax.persistence.*;

@Entity
@Table(name = "task_types")
public class TaskType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @NaturalId
    @Column(length = 60)
    private TaskTypeName name;

    public TaskType() {
    }

    public TaskType(TaskTypeName name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TaskTypeName getName() {
        return name;
    }

    public void setName(TaskTypeName name) {
        this.name = name;
    }
}
