package com.taskboard.payloadConverter;

import com.taskboard.model.Board;
import com.taskboard.model.Task;
import com.taskboard.model.TaskStateName;
import com.taskboard.payload.BoardDetailedViewResponse;
import com.taskboard.payload.TaskResponse;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BoardMapper {
    public static BoardDetailedViewResponse boardToBoardDetailedView(Board board) {
        List<Task> tasks = board.getTasks()
                .stream()
                .filter(task -> !task.isArchived())
                .collect(Collectors.toList());
        Set<TaskResponse> toDo = new HashSet<>();
        Set<TaskResponse> inProgress = new HashSet<>();
        Set<TaskResponse> done = new HashSet<>();
        tasks.forEach(
                item -> {
                    TaskStateName state = item.getState().getName();
                    TaskResponse taskResponse = new TaskResponse(item);
                    if(state.equals(TaskStateName.TASK_STATE_TO_DO)) toDo.add(taskResponse);
                    else if(state.equals(TaskStateName.TASK_STATE_IN_PROGRESS)) inProgress.add(taskResponse);
                    else if(state.equals(TaskStateName.TASK_STATE_DONE)) done.add(taskResponse);
                }
        );
        return new BoardDetailedViewResponse(board.getName(), board.getDescription(), toDo, inProgress, done);
    }
}
