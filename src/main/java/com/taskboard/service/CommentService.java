package com.taskboard.service;

import com.taskboard.model.Comment;
import com.taskboard.model.Task;
import com.taskboard.model.User;
import com.taskboard.repository.CommentRepository;
import com.taskboard.repository.TaskRepository;
import com.taskboard.repository.UserRepository;
import com.taskboard.security.UserPrincipal;
import javassist.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public void addNewComment(UserPrincipal userPrincipal, Long taskId, String content) throws NotFoundException {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new NotFoundException("task not exist"));
        User author = userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new NotFoundException("user not exist"));

        Comment comment = Comment.builder()
                .task(task)
                .author(author)
                .content(content)
                .build();
        commentRepository.save(comment);
    }

    public List<Comment> findCommentsByTaskId(Long taskId) throws NotFoundException {
        taskRepository.findById(taskId).orElseThrow(() -> new NotFoundException("task not exist"));
        return commentRepository.findByTaskIdOrderById(taskId);
    }

    public void updateComment(Long commentId, String content) throws NotFoundException {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("comment not exist"));
        comment.setContent(content);
        commentRepository.save(comment);
    }
}
