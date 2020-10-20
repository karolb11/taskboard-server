package com.taskboard.controller;

import com.taskboard.model.Comment;
import com.taskboard.model.Task;
import com.taskboard.payload.ApiResponse;
import com.taskboard.payload.CommentResponse;
import com.taskboard.payload.TaskResponse;
import com.taskboard.payloadConverter.CommentMapper;
import com.taskboard.security.CurrentUser;
import com.taskboard.security.EditCommentSecurityGuard;
import com.taskboard.security.UserPrincipal;
import com.taskboard.service.CommentService;
import com.taskboard.utils.ResourceIdUtils;
import javassist.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping("/api/comment")
@AllArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final CommentMapper commentMapper;
    private final ResourceIdUtils resourceIdUtils;
    private final EditCommentSecurityGuard editCommentSecurityGuard;

    @GetMapping()
    @PreAuthorize("hasAuthority('board'+@resourceIdUtils.getBoardIdByTaskId(#taskId)+':'+'LOCAL_ROLE_VIEWER')" +
            "or hasAuthority('board'+@resourceIdUtils.getBoardIdByTaskId(#taskId)+':'+'LOCAL_ROLE_USER')" +
            "or hasAuthority('board'+@resourceIdUtils.getBoardIdByTaskId(#taskId)+':'+'LOCAL_ROLE_OWNER')")
    public ResponseEntity<?> getCommentsByTaskId(
            @PathParam("taskId") Long taskId
    ) {
        try {
            List<CommentResponse> res = commentService.findCommentsByTaskId(taskId)
                    .stream()
                    .map(commentMapper::CommentToCommentResponse)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(res, HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping()
    @PreAuthorize("hasAuthority('board'+@resourceIdUtils.getBoardIdByTaskId(#taskId)+':'+'LOCAL_ROLE_VIEWER')" +
            "or hasAuthority('board'+@resourceIdUtils.getBoardIdByTaskId(#taskId)+':'+'LOCAL_ROLE_USER')" +
            "or hasAuthority('board'+@resourceIdUtils.getBoardIdByTaskId(#taskId)+':'+'LOCAL_ROLE_OWNER')")
    public ResponseEntity<?> addNewComment(
            @CurrentUser UserPrincipal currentUser,
            @PathParam("taskId") Long taskId,
            @RequestBody String content
    ) {
        try {
            commentService.addNewComment(currentUser, taskId, content);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{commentId}")
    @PreAuthorize("hasAuthority('ROLE_MOD') or hasAuthority('ROLE_ADMIN')" +
            "or hasAuthority('board'+@resourceIdUtils.getBoardIdByCommentId(#commentId)+':'+'LOCAL_ROLE_OWNER')" +
            "or @editCommentSecurityGuard.isCommentAuthor(#commentId, #currentUser.getId())")
    public ResponseEntity<?> updateComment(@CurrentUser UserPrincipal currentUser,
                                           @PathVariable Long commentId,
                                           @RequestBody String content) {
        try {
            commentService.updateComment(commentId, content);
            ApiResponse res = new ApiResponse(true, "Comment edited");
            return new ResponseEntity<>(res, HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
