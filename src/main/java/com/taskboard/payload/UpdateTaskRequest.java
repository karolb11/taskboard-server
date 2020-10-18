package com.taskboard.payload;

import com.taskboard.model.TaskPriority;
import com.taskboard.model.TaskState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTaskRequest {
    private String name;
    private String description;
    @Nullable
    private Long assignedUserId;
    private Long priorityId;
    private Long stateId;
    private List<SubTaskRequest> SubTasks;
}
