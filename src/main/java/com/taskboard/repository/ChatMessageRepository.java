package com.taskboard.repository;

import com.taskboard.model.ChatMessage;
import com.taskboard.payload.ChatMessageResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    public List<ChatMessage> findAllByBoardIdOrderByCreatedAt(Long boardId);
    public List<ChatMessage> findTop5ByBoardIdOrderByCreatedAtDesc(Long boardId);
}
