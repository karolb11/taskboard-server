package com.taskboard.controller;

import com.taskboard.payload.ChatMessageRequest;
import com.taskboard.payload.ChatMessageResponse;
import com.taskboard.payloadConverter.ChatMessageMapper;
import com.taskboard.security.CurrentUser;
import com.taskboard.security.UserPrincipal;
import com.taskboard.service.ChatService;
import javassist.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/chat")
@AllArgsConstructor
public class ChatController {
    final private ChatService chatService;

    @PostMapping()
    @PreAuthorize("hasAuthority('board'+#chatMessageRequest.boardId+':'+'LOCAL_ROLE_VIEWER')" +
            "or hasAuthority('board'+#chatMessageRequest.boardId+':'+'LOCAL_ROLE_USER')" +
            "or hasAuthority('board'+#chatMessageRequest.boardId+':'+'LOCAL_ROLE_OWNER')")
    public ResponseEntity<?> addNewMessage(@CurrentUser UserPrincipal currentUser,
                                           @RequestBody ChatMessageRequest chatMessageRequest) {
        try {
            chatService.addNewMessage(currentUser, chatMessageRequest);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping()
    @PreAuthorize("hasAuthority('board'+#boardId+':'+'LOCAL_ROLE_VIEWER')" +
            "or hasAuthority('board'+#boardId+':'+'LOCAL_ROLE_USER')" +
            "or hasAuthority('board'+#boardId+':'+'LOCAL_ROLE_OWNER')")
    public ResponseEntity<?> getMessagesByBoardId(@CurrentUser UserPrincipal currentUser,
                                                  @PathParam("boardId") Long boardId) {
        List<ChatMessageResponse> res = chatService.getMessagesByBoardId(boardId)
                .stream()
                .map(ChatMessageMapper::chatMessageToChatMessageResponse)
                .collect(Collectors.toList());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}
