package com.taskboard.repository;

import com.taskboard.model.ChatMessage;
import com.taskboard.payload.ChatMessageResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findAllByBoardIdOrderByCreatedAt(Long boardId);
    List<ChatMessage> findTop5ByBoardIdOrderByCreatedAtDesc(Long boardId);
}
