package com.taskboard.payload;

import com.taskboard.model.Task;
import com.taskboard.model.TaskPriority;
import com.taskboard.model.TaskPriorityName;
import com.taskboard.model.TaskState;
import com.taskboard.payloadConverter.SubTaskMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskResponse {
    Long id;
    String name;
    String description;
    BoardUserResponse assignedUser;
    TaskState state;
    TaskPriority priority;
    List<SubTaskResponse> subTasks;

    public TaskResponse(Task task) {
        this.id = task.getId();
        this.name = task.getName();
        this.description = task.getDescription();
        if(task.getAssignedUser() != null) {
            this.assignedUser = new BoardUserResponse(
                    task.getAssignedUser().getId(),
                    task.getAssignedUser().getName());
        }
        this.state = task.getState();
        this.priority = task.getPriority();
        this.subTasks = task.getSubTasks()
                .stream()
                .map(SubTaskMapper::subTaskToSubTaskResponse)
                .collect(Collectors.toList());
    }
}


