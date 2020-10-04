package com.taskboard.controller;

import com.taskboard.model.Task;
import com.taskboard.model.TaskPriority;
import com.taskboard.model.TaskState;
import com.taskboard.payload.CreateTaskRequest;
import com.taskboard.payload.SubscribedTaskResponse;
import com.taskboard.payload.UpdateTaskRequest;
import com.taskboard.payload.TaskResponse;
import com.taskboard.payloadConverter.SubscriptionMapper;
import com.taskboard.payloadConverter.TaskMapper;
import com.taskboard.security.CurrentUser;
import com.taskboard.security.UserPrincipal;
import com.taskboard.service.TaskService;
import javassist.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@PreAuthorize("hasRole('USER')")
@RequestMapping("/api/task")
public class TaskController {
    final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<?> createTask(@CurrentUser UserPrincipal currentUser,
                                        @RequestBody CreateTaskRequest createTaskRequest) {
        try {
            taskService.createTask(createTaskRequest, currentUser.getId());
            return new ResponseEntity<>("Task created!", HttpStatus.CREATED);
        }catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<?> getTaskById(@CurrentUser UserPrincipal currentUser,
                                         @PathVariable("taskId") Long taskId) {
        try {
            Task task = taskService.getTaskById(taskId);
            TaskResponse response = new TaskResponse(task);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<?> updateTask(@CurrentUser UserPrincipal currentUser,
                                        @PathVariable("taskId") Long taskId,
                                        @RequestBody UpdateTaskRequest updateTaskRequest) {
        try {
            Task task = taskService.updateTask(taskId, updateTaskRequest);
            TaskResponse response = new TaskResponse(task);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/subscribed")
    public ResponseEntity<?> getActiveSubscribedTasksByUser(@CurrentUser UserPrincipal currentUser) {
        List<Task> tasks = taskService.getActiveSubscribedTasksByUser(currentUser.getId());
        List<SubscribedTaskResponse> response =
                TaskMapper.TaskListToSubscribedTaskList(tasks);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/priority")
    public ResponseEntity<?> getTaskPriorities() {
        List<TaskPriority> taskPriorities = taskService.getTaskPriorities();
        return new ResponseEntity<>(taskPriorities, HttpStatus.OK);
    }

    @GetMapping("state")
    public ResponseEntity<?> getTaskStates() {
        List<TaskState> taskStates = taskService.getTaskStates();
        return new ResponseEntity<>(taskStates, HttpStatus.OK);
    }
}
