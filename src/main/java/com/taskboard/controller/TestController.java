package com.taskboard.controller;

import com.taskboard.exception.AppException;
import com.taskboard.payload.BoardWidgetResponse;
import com.taskboard.security.UserBoardPermissionsValidator;
import com.taskboard.model.*;
import com.taskboard.payload.BoardLocalGroupUserLinkResponse;
import com.taskboard.repository.*;
import com.taskboard.security.CurrentUser;
import com.taskboard.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    BoardLocalGroupUserLinkRepository boardLocalGroupUserLinkRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    LocalRoleRepository localRoleRepository;

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    TaskStateRepository taskStateRepository;

    @Autowired
    TaskTypeRepository taskTypeRepository;

    @Autowired
    TaskPriorityRepository taskPriorityRepository;

    @Autowired
    UserBoardPermissionsValidator userBoardPermissionsValidator;

    @GetMapping("/board/{boardId}")
    public ResponseEntity<?> getBoardById(@PathVariable("boardId") Long boardId) {
        Board board = boardRepository.findById(boardId).get();
        BoardWidgetResponse boardWidgetResponse = new BoardWidgetResponse(board.getId(), board.getName(), board.getDescription());
        return new ResponseEntity<>(boardWidgetResponse, HttpStatus.OK);
    }

    @PostMapping("/board/{boardId}/checkOwnerAccess")
    @Secured({"ROLE_USER"})
    public @ResponseBody
    String checkOwnerAccess(@CurrentUser UserPrincipal currentUser,
                             @PathVariable("boardId") Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new AppException("Board not found"));
        User user = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new AppException("User not found"));
        if(userBoardPermissionsValidator.hasRequiredPerms(user, board, LocalRoleName.LOCAL_ROLE_OWNER)) return "You have access to this board!";
        else return "You don't have access :C";
    }

    @PostMapping("/board/{boardId}/checkOwnerAccess1")
    @Secured({"ROLE_USER"})
    public @ResponseBody
    String checkOwnerAccess1(@CurrentUser UserPrincipal currentUser,
                       @PathVariable("boardId") Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new AppException("Board not found"));
        User user = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new AppException("User not found"));
        if(userBoardPermissionsValidator.hasRequiredPerms(user, board, LocalRoleName.LOCAL_ROLE_OWNER)) return "You have access to this board!";

        return "You don't have access :C";
    }

    @PostMapping("/{boardId}/checkRoles")
    @Secured({"ROLE_USER"})
    public @ResponseBody
    Set<BoardLocalGroupUserLinkResponse> checkRoles(@CurrentUser UserPrincipal currentUser,
                                                    @PathVariable("boardId") Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new AppException("Board not found"));
        User user = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new AppException("User not found"));
        Iterable<BoardLocalGroupUserLink> boardLocalGroupUserLinks =
                boardLocalGroupUserLinkRepository.findByUserAndBoard(user, board);
        Set<BoardLocalGroupUserLinkResponse> response = new HashSet<>();
        boardLocalGroupUserLinks.forEach(
                i -> response.add(new BoardLocalGroupUserLinkResponse(
                        i.getBoard().getId(),
                        i.getLocalRole().getId(),
                        i.getUser().getId()))
        );
        return response;
    }

}
