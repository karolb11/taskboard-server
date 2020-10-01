package com.taskboard.payload;

import lombok.Data;

@Data
public class SubTaskRequest {
    private Long id;
    private String name;
    private String description;
    private boolean done;
}
