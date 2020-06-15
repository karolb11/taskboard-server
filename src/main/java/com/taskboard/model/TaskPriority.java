package com.taskboard.model;

import com.taskboard.model.TaskPriorityName;
import com.taskboard.model.TaskStateName;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;

@Entity
@Table(name= "task_priorities")
public class TaskPriority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @NaturalId
    @Column(length = 60)
    private TaskPriorityName name;

    public TaskPriority(TaskPriorityName name) {
        this.name = name;
    }

    public TaskPriority() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TaskPriorityName getName() {
        return name;
    }

    public void setName(TaskPriorityName name) {
        this.name = name;
    }
}
