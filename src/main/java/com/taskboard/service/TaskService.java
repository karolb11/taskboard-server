package com.taskboard.service;

import com.taskboard.model.*;
import com.taskboard.payload.BoardUserResponse;
import com.taskboard.payload.CreateTaskRequest;
import com.taskboard.payload.SubscribedTaskResponse;
import com.taskboard.payload.UpdateTaskRequest;
import com.taskboard.payloadConverter.SubTaskMapper;
import com.taskboard.payloadConverter.SubscriptionMapper;
import com.taskboard.repository.*;
import com.taskboard.security.UserBoardPermissionsValidator;
import javassist.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {
    final TaskRepository taskRepository;
    final TaskPriorityRepository taskPriorityRepository;
    final TaskStateRepository taskStateRepository;
    final UserRepository userRepository;
    final BoardRepository boardRepository;
    final UserBoardPermissionsValidator userBoardPermissionsValidator;
    final SubscriptionService subscriptionService;

    public TaskService(TaskRepository taskRepository,
                       TaskPriorityRepository taskPriorityRepository,
                       TaskStateRepository taskStateRepository,
                       UserRepository userRepository,
                       BoardRepository boardRepository,
                       UserBoardPermissionsValidator userBoardPermissionsValidator,
                       SubscriptionService subscriptionService) {
        this.taskRepository = taskRepository;
        this.taskPriorityRepository = taskPriorityRepository;
        this.taskStateRepository = taskStateRepository;
        this.userRepository = userRepository;
        this.boardRepository = boardRepository;
        this.userBoardPermissionsValidator = userBoardPermissionsValidator;
        this.subscriptionService = subscriptionService;
    }

    @Transactional
    public void createTask(CreateTaskRequest createTaskRequest, Long authorId) throws NotFoundException {
        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new NotFoundException("user not found"));
        User assignedUser = userRepository.findById(createTaskRequest.getAssignedUserId())
                .orElse(null);
        Board board = boardRepository.findBoardById(createTaskRequest.getBoardId())
                .orElseThrow(() -> new NotFoundException("board not found"));
        TaskPriority priority = taskPriorityRepository.findById(createTaskRequest.getPriorityId())
                .orElseThrow(() -> new NotFoundException("priority not found"));
        TaskState state = taskStateRepository.findById(createTaskRequest.getStateId())
                .orElseThrow(() -> new NotFoundException("state not found"));
        if(!userBoardPermissionsValidator.validate(assignedUser, board, LocalRoleName.LOCAL_ROLE_USER))
        throw new NotFoundException("assigned user is not a member of this board");

        List<SubTask> subTasks = createTaskRequest.getSubTasks().stream().map(
                subTaskRequest -> SubTask.builder()
                        .name(subTaskRequest.getName())
                        .description(subTaskRequest.getDescription())
                        .build()
        ).collect(Collectors.toList());

        Task task = new Task(
                createTaskRequest.getName(),
                createTaskRequest.getDescription(),
                board,
                author,
                assignedUser,
                priority,
                state,
                subTasks
        );

        taskRepository.save(task);
    }

    @Transactional
    public Task updateTask(Long taskId, UpdateTaskRequest request) throws NotFoundException {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new NotFoundException("task not found"));
        User assignedUser = null;
        if(request.getAssignedUserId() != null) {
            assignedUser = userRepository.findById(request.getAssignedUserId())
                    .orElseThrow(() -> new NotFoundException("user not found"));
        }
        TaskPriority priority = taskPriorityRepository.findById(request.getPriorityId())
                .orElseThrow(() -> new NotFoundException("priority not found"));
        TaskState state = taskStateRepository.findById(request.getStateId())
                .orElseThrow(() -> new NotFoundException("state not found"));

        List<SubTask> subTasks = SubTaskMapper.subTaskRequestListToSubTaskList(
                request.getSubTasks()
        );

        task.setName(request.getName());
        task.setDescription(request.getDescription());
        task.setSubTasks(subTasks);
        task.setPriority(priority);
        task.setState(state);
        task.setAssignedUser(assignedUser);
        return taskRepository.save(task);
    }

    public Task getTaskById(Long id) throws NotFoundException {
        return taskRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Task not found"));
    }

    public List<TaskPriority> getTaskPriorities() {
        return taskPriorityRepository.findAllByOrderById();
    }
    public List<TaskState> getTaskStates() {
        return taskStateRepository.findAllByOrderById();
    }

    public List<Task> getActiveSubscribedTasksByUser(Long userId) {
        List<Subscription> subscriptions = subscriptionService.findSubscriptionByUserId(userId);
        return subscriptions.stream()
                .map(Subscription::getTask)
                .filter(i -> i.getState().getName() != TaskStateName.TASK_STATE_DONE)
                .collect(Collectors.toList());
    }
}
