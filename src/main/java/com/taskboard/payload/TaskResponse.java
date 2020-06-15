package com.taskboard.payload;

import com.taskboard.model.Task;
import lombok.Data;

@Data
public class TaskResponse {
    Long id;
    String name;

    public TaskResponse() {
    }

    public TaskResponse(Task task) {
        this.id = task.getId();
        this.name = task.getName();
    }

    public TaskResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}


