package com.taskboard.payload;

import com.taskboard.model.TaskPriority;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskRequest {
    private String name;
    private String description;
    private Long boardId;
    private Long assignedUserId;
    private Long priorityId;
    private Long stateId;
}
