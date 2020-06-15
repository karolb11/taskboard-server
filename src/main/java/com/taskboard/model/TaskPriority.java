package com.taskboard.model;

import com.taskboard.model.TaskPriorityName;
import com.taskboard.model.TaskStateName;
import lombok.Data;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;

@Entity
@Table(name= "task_priorities")
@Data
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

}
