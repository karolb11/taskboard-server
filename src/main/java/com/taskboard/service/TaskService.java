package com.taskboard.service;

import com.taskboard.model.*;
import com.taskboard.payload.TaskRequest;
import com.taskboard.repository.*;
import com.taskboard.security.UserBoardPermissionsValidator;
import javassist.NotFoundException;
import org.aspectj.lang.annotation.Around;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TaskService {
    final
    TaskRepository taskRepository;
    final
    TaskPriorityRepository taskPriorityRepository;
    final
    TaskStateRepository taskStateRepository;
    final
    UserRepository userRepository;
    final
    BoardRepository boardRepository;
    final
    UserBoardPermissionsValidator userBoardPermissionsValidator;

    public TaskService(TaskRepository taskRepository, TaskPriorityRepository taskPriorityRepository, TaskStateRepository taskStateRepository, UserRepository userRepository, BoardRepository boardRepository, UserBoardPermissionsValidator userBoardPermissionsValidator) {
        this.taskRepository = taskRepository;
        this.taskPriorityRepository = taskPriorityRepository;
        this.taskStateRepository = taskStateRepository;
        this.userRepository = userRepository;
        this.boardRepository = boardRepository;
        this.userBoardPermissionsValidator = userBoardPermissionsValidator;
    }

    @Transactional
    public void createTask(TaskRequest taskRequest, Long authorId) throws NotFoundException {
        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new NotFoundException("user not found"));
        User assignedUser = userRepository.findById(taskRequest.getAssignedUserId())
                .orElseThrow(() -> new NotFoundException("user not found"));
        Board board = boardRepository.findBoardById(taskRequest.getBoardId())
                .orElseThrow(() -> new NotFoundException("board not found"));
        TaskPriority priority = taskPriorityRepository.findById(taskRequest.getPriorityId())
                .orElseThrow(() -> new NotFoundException("priority not found"));
        TaskState state = taskStateRepository.findById(taskRequest.getStateId())
                .orElseThrow(() -> new NotFoundException("state not found"));
        if(!userBoardPermissionsValidator.validate(assignedUser, board, LocalRoleName.LOCAL_ROLE_USER))
        throw new NotFoundException("assigned user is not a member of this board");
        Task task = new Task(
                taskRequest.getName(),
                taskRequest.getDescription(),
                board,
                author,
                assignedUser,
                priority,
                state
        );
        taskRepository.save(task);
    }

    public List<TaskPriority> getTaskPriorities() {
        return taskPriorityRepository.findAllByOrderById();
    }
    public List<TaskState> getTaskStates() {
        return taskStateRepository.findAllByOrderById();
    }
}
