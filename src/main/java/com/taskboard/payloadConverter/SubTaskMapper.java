package com.taskboard.payloadConverter;

import com.taskboard.model.SubTask;
import com.taskboard.payload.SubTaskRequest;
import com.taskboard.payload.SubTaskResponse;
import org.springframework.stereotype.Service;

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
}
