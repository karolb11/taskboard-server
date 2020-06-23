package com.taskboard.model;

import com.taskboard.model.audit.DateAudit;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Entity
@Table(name = "boards")
@Data
public class Board extends DateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 100)
    private String name;

    @Size(max = 1000) //@Size(max = 1000, message = "khym")
    private String description;
    
    @OneToMany
    @JoinColumn(name = "board_id")
    private Set<BoardLocalGroupUserLink> boardLocalGroupUserLinks = new HashSet<>();

    @OneToMany
    @JoinColumn(name="board_id")
    private Set<Task> tasks = new HashSet<>();

    public Board() {
    }

    public Board(@NotBlank @Size(max = 100) String name, @Size(max = 1000) String description) {
        this.name = name;
        this.description = description;
    }

}
