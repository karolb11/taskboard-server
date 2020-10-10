package com.taskboard.utils;

import com.taskboard.model.BoardLocalGroupUserLink;
import com.taskboard.model.Comment;
import com.taskboard.model.Task;
import com.taskboard.repository.BoardLocalGroupUserLinkRepository;
import com.taskboard.repository.CommentRepository;
import com.taskboard.repository.TaskRepository;
import javassist.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ResourceIdUtils {
    private final BoardLocalGroupUserLinkRepository boardLocalGroupUserLinkRepository;
    public final CommentRepository commentRepository;
    public final TaskRepository taskRepository;

    public Long getBoardIdByInvitationId(Long invitationId) throws NotFoundException {
        BoardLocalGroupUserLink boardLocalGroupUserLink = boardLocalGroupUserLinkRepository.findById(invitationId)
                .orElseThrow(() -> new NotFoundException("Invitation not exist"));
        return boardLocalGroupUserLink.getBoard().getId();
    }

    public Long getBoardIdByCommentId(Long commentId) throws NotFoundException {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment not exist"));
        return comment.getTask().getBoard().getId();
    }

    public Long getBoardIdByTaskId(Long taskId) throws NotFoundException {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new NotFoundException("Task not exist"));
        return task.getBoard().getId();
    }
}
