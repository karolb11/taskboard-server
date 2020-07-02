package com.taskboard.payload;

import com.taskboard.model.Task;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskResponse {
    Long id;
    String name;
    String description;
    String assignedUser;
    String state;

    public TaskResponse(Task task) {
        this.id = task.getId();
        this.name = task.getName();
        this.description = task.getDescription();
        if(task.getAssignedUser() != null) {
            this.assignedUser = task.getAssignedUser().getName();
        }
        this.state = task.getState().getName().toString();
    }
}


