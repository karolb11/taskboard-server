package com.taskboard.payload;

import lombok.Data;

@Data
public class TaskRequest {
    private String name;
    private String description;

    public TaskRequest() {
    }
}
