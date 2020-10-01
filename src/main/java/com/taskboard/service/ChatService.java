package com.taskboard.service;

import com.taskboard.model.Board;
import com.taskboard.model.ChatMessage;
import com.taskboard.model.User;
import com.taskboard.payload.ChatMessageRequest;
import com.taskboard.payload.ChatMessageResponse;
import com.taskboard.repository.BoardRepository;
import com.taskboard.repository.ChatMessageRepository;
import com.taskboard.repository.UserRepository;
import com.taskboard.security.UserPrincipal;
import javassist.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ChatService {
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;

    public void addNewMessage(UserPrincipal userPrincipal, Long boardId, String content) throws NotFoundException {
        User author = userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new NotFoundException("User not found"));
        Board board = boardRepository.findBoardById(boardId)
                .orElseThrow(() -> new NotFoundException("Board not found"));
        ChatMessage message = ChatMessage.builder()
                .author(author)
                .board(board)
                .content(content)
                .build();
        chatMessageRepository.save(message);
    }
    public void addNewMessage(UserPrincipal userPrincipal, ChatMessageRequest chatMessageRequest) throws NotFoundException {
        addNewMessage(
                userPrincipal,
                chatMessageRequest.getBoardId(),
                chatMessageRequest.getContent());
    }

    public List<ChatMessage> getMessagesByBoardId(Long boardId) {
        return chatMessageRepository.findTop5ByBoardIdOrderByCreatedAtDesc(boardId);
    }
}
