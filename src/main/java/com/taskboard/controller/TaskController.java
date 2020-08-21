package com.taskboard.controller;

import com.taskboard.model.Task;
import com.taskboard.model.TaskPriority;
import com.taskboard.model.TaskState;
import com.taskboard.payload.TaskRequest;
import com.taskboard.payload.TaskResponse;
import com.taskboard.security.CurrentUser;
import com.taskboard.security.UserPrincipal;
import com.taskboard.service.TaskService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@PreAuthorize("hasRole('USER')")
@RequestMapping("/api/task")
public class TaskController {
    final
    TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<?> createTask(@CurrentUser UserPrincipal currentUser, @RequestBody TaskRequest taskRequest) {
        try {
            taskService.createTask(taskRequest, currentUser.getId());
            return new ResponseEntity<>("Task created!", HttpStatus.CREATED);
        }catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<?> getTaskById(@CurrentUser UserPrincipal currentUser, @PathVariable("taskId") Long taskId) {
        try {
            Task task = taskService.getTaskById(taskId);
            TaskResponse response = new TaskResponse(task);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> updateTask(@CurrentUser UserPrincipal currentUser,
                                        @PathVariable("taskId") Long taskId,
                                        @RequestBody TaskRequest taskRequest) {
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
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
