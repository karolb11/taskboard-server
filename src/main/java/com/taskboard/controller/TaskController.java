package com.taskboard.controller;

import com.taskboard.model.Task;
import com.taskboard.model.TaskPriority;
import com.taskboard.model.TaskState;
import com.taskboard.payload.*;
import com.taskboard.payloadConverter.TaskMapper;
import com.taskboard.security.CurrentUser;
import com.taskboard.security.UserPrincipal;
import com.taskboard.service.TaskService;
import com.taskboard.utils.ResourceIdUtils;
import javassist.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping("/api/task")
@AllArgsConstructor
public class TaskController {
    private final TaskService taskService;
    private final ResourceIdUtils resourceIdUtils;

    @PostMapping
    @PreAuthorize("hasAuthority('board'+#createTaskRequest.getBoardId()+':'+'LOCAL_ROLE_USER')" +
            "or hasAuthority('board'+#createTaskRequest.getBoardId()+':'+'LOCAL_ROLE_OWNER')")
    public ResponseEntity<?> createTask(@CurrentUser UserPrincipal currentUser,
                                        @RequestBody CreateTaskRequest createTaskRequest) {
        try {
            taskService.createTask(createTaskRequest, currentUser.getId());
            ApiResponse res = new ApiResponse(true, "Task created");
            return new ResponseEntity<>(res, HttpStatus.OK);
        }catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{taskId}")
    @PreAuthorize("hasAuthority('board'+@resourceIdUtils.getBoardIdByTaskId(#taskId)+':'+'LOCAL_ROLE_VIEWER')" +
            "or hasAuthority('board'+@resourceIdUtils.getBoardIdByTaskId(#taskId)+':'+'LOCAL_ROLE_USER')" +
            "or hasAuthority('board'+@resourceIdUtils.getBoardIdByTaskId(#taskId)+':'+'LOCAL_ROLE_OWNER')")
    public ResponseEntity<?> getTaskById(@PathVariable("taskId") Long taskId) {
        try {
            Task task = taskService.getTaskById(taskId);
            TaskResponse response = new TaskResponse(task);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{taskId}")
    @PreAuthorize("hasAuthority('board'+@resourceIdUtils.getBoardIdByTaskId(#taskId)+':'+'LOCAL_ROLE_USER')" +
            "or hasAuthority('board'+@resourceIdUtils.getBoardIdByTaskId(#taskId)+':'+'LOCAL_ROLE_OWNER')")
    public ResponseEntity<?> editTask(@PathVariable("taskId") Long taskId,
                                      @RequestBody UpdateTaskRequest updateTaskRequest) {
        try {
            Task task = taskService.updateTask(taskId, updateTaskRequest);
            TaskResponse response = new TaskResponse(task);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/{taskId}/archive")
    @PreAuthorize("hasAuthority('ROLE_MOD') or hasAuthority('ROLE_ADMIN')" +
            "or hasAuthority('board'+@resourceIdUtils.getBoardIdByTaskId(#taskId)+':'+'LOCAL_ROLE_OWNER')")
    public ResponseEntity<?> archiveTask(@PathVariable("taskId") Long taskId) {
        try {
            taskService.archiveTaskById(taskId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/subscribed")
    //not needed to be secured
    public ResponseEntity<?> getActiveSubscribedTasksByUser(@CurrentUser UserPrincipal currentUser) {
        List<Task> tasks = taskService.getActiveSubscribedTasksByUser(currentUser.getId());
        List<SubscribedTaskResponse> response =
                TaskMapper.TaskListToSubscribedTaskList(tasks);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/priority")
    //not needed to be secured
    public ResponseEntity<?> getTaskPriorities() {
        List<TaskPriority> taskPriorities = taskService.getTaskPriorities();
        return new ResponseEntity<>(taskPriorities, HttpStatus.OK);
    }

    @GetMapping("state")
    //not needed to be secured
    public ResponseEntity<?> getTaskStates() {
        List<TaskState> taskStates = taskService.getTaskStates();
        return new ResponseEntity<>(taskStates, HttpStatus.OK);
    }
}
