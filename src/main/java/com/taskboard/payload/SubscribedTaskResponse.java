package com.taskboard.payload;

import com.taskboard.model.TaskPriority;
import com.taskboard.model.TaskState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubscribedTaskResponse {
    Long id;
    Long boardId;
    String name;
    String description;
    String assignedUser;
    TaskState state;
    TaskPriority priority;
    Instant updatedAt;
}
