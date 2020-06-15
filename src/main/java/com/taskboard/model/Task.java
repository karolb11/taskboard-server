package com.taskboard.model;

import com.taskboard.model.audit.DateAudit;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name = "tasks")
@Data
public class Task extends DateAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 3, max = 100)
    private String name;

    @Size(min = 3, max = 1000)
    private String description;

    @ManyToOne
    @JoinColumn(name = "state_id", nullable = false)
    private TaskState state;

    @ManyToOne
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @ManyToOne
    @JoinColumn(name = "priority_id", nullable = false)
    private TaskPriority priority;

    public Task() {

    }

    public Task(@Size(min = 3, max = 100) String name, @Size(min = 3, max = 1000) String description, Board board, User author) {
        this.name = name;
        this.description = description;
        this.board = board;
        this.author = author;
    }

}
