package com.taskboard.security;

import com.taskboard.model.Comment;
import com.taskboard.repository.CommentRepository;
import javassist.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class EditCommentSecurityGuard {
    private final CommentRepository commentRepository;

    public boolean isCommentAuthor(Long commentId, Long userId) throws NotFoundException {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment not found"));
        return comment.getAuthor().getId().equals(userId);
    }
}
