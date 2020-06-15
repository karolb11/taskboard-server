package com.taskboard.payload;


import java.util.HashSet;
import java.util.Set;

public class BoardViewResponse {
    private String name;
    private String description;
    private Set<TaskResponse> toDo = new HashSet<>();
    private Set<TaskResponse> inProgress = new HashSet<>();
    private Set<TaskResponse> done = new HashSet<>();

    public BoardViewResponse() {
    }

    public BoardViewResponse(String name, String description, Set<TaskResponse> toDo, Set<TaskResponse> inProgress, Set<TaskResponse> done) {
        this.name = name;
        this.description = description;
        this.toDo = toDo;
        this.inProgress = inProgress;
        this.done = done;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public Set<TaskResponse> getToDo() {
        return toDo;
    }

    public void setToDo(Set<TaskResponse> toDo) {
        this.toDo = toDo;
    }

    public Set<TaskResponse> getInProgress() {
        return inProgress;
    }

    public void setInProgress(Set<TaskResponse> inProgress) {
        this.inProgress = inProgress;
    }

    public Set<TaskResponse> getDone() {
        return done;
    }

    public void setDone(Set<TaskResponse> done) {
        this.done = done;
    }
}
