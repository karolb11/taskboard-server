package com.taskboard.payload;


import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class BoardDetailedViewResponse {
    private String name;
    private String description;
    private Set<TaskResponse> toDo = new HashSet<>();
    private Set<TaskResponse> inProgress = new HashSet<>();
    private Set<TaskResponse> done = new HashSet<>();

    public BoardDetailedViewResponse() {
    }

    public BoardDetailedViewResponse(String name, String description, Set<TaskResponse> toDo, Set<TaskResponse> inProgress, Set<TaskResponse> done) {
        this.name = name;
        this.description = description;
        this.toDo = toDo;
        this.inProgress = inProgress;
        this.done = done;
    }
}
