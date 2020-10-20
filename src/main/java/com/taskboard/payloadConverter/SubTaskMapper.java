package com.taskboard.payloadConverter;

import com.taskboard.model.SubTask;
import com.taskboard.payload.SubTaskRequest;
import com.taskboard.payload.SubTaskResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubTaskMapper {
    public static SubTaskResponse subTaskToSubTaskResponse(SubTask subTask) {
        return new SubTaskResponse(
                subTask.getId(),
                subTask.getName(),
                subTask.getDescription(),
                subTask.isDone()
        );
    }
    public static List<SubTask> subTaskRequestListToSubTaskList(
            List<SubTaskRequest> subTaskRequestList) {
        return subTaskRequestList.stream().map(
                subTaskRequest -> SubTask.builder()
                        .id(subTaskRequest.getId())
                        .name(subTaskRequest.getName())
                        .description(subTaskRequest.getDescription())
                        .done(subTaskRequest.isDone())
                        .build()
        ).collect(Collectors.toList());
    }
}
