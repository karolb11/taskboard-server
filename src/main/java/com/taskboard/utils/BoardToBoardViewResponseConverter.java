package com.taskboard.utils;

import com.taskboard.model.*;
import com.taskboard.payload.BoardViewResponse;
import com.taskboard.payload.TaskResponse;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class BoardToBoardViewResponseConverter {

    public BoardViewResponse convert(Board board) {
        Set<Task> tasks = board.getTasks();
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
        return new BoardViewResponse(board.getName(), board.getDescription(), toDo, inProgress, done);

    }
}
