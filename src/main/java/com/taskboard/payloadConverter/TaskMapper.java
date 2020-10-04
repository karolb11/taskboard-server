package com.taskboard.payloadConverter;

import com.taskboard.model.Task;
import com.taskboard.payload.SubscribedTaskResponse;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class TaskMapper {
    public static List<SubscribedTaskResponse> TaskListToSubscribedTaskList(List<Task> taskList) {
        return taskList.stream()
                .map(i -> new SubscribedTaskResponse(
                        i.getId(),
                        i.getBoard().getId(),
                        i.getName(),
                        i.getDescription(),
                        i.getAssignedUser().getName(),
                        i.getState(),
                        i.getPriority(),
                        i.getUpdatedAt()
                ))
                .collect(Collectors.toList());
    }
}
