package com.taskboard.payload;

import com.taskboard.model.TaskPriority;
import com.taskboard.model.TaskState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EditTaskRequest {
    private String name;
    private String description;
    private Long boardId;
    private AssignedUser assignedUser;
    private TaskPriority priority;
    private TaskState state;
    private List<SubTaskRequest> subTasks;
}
