package com.taskboard.controller;

import com.taskboard.exception.AppException;
import com.taskboard.payload.*;
import com.taskboard.security.UserBoardPermissionsValidator;
import com.taskboard.model.*;
import com.taskboard.repository.*;
import com.taskboard.security.CurrentUser;
import com.taskboard.security.UserPrincipal;
import com.taskboard.utils.BoardToBoardViewResponseConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api/board")
public class BoardController {

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

    @Autowired
    BoardToBoardViewResponseConverter boardToBoardViewResponseConverter;

    @PostMapping("/add")
    @Transactional
    public @ResponseBody
    ResponseEntity<?> addNewBoard(@CurrentUser UserPrincipal currentUser,
                               @RequestBody @Valid BoardRequest boardRequest) {
        Board board = new Board(boardRequest.getName(), boardRequest.getDescription());
        User author = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new AppException("User not found"));
        LocalRole localRole = localRoleRepository.findByName(LocalRoleName.LOCAL_ROLE_OWNER)
                .orElseThrow(() -> new AppException("Local role not found"));
        BoardLocalGroupUserLink boardLocalGroupUserLink = new BoardLocalGroupUserLink(board, localRole, author);

        boardRepository.save(board);
        boardLocalGroupUserLinkRepository.save(boardLocalGroupUserLink);

        return new ResponseEntity<>("Board created!", HttpStatus.CREATED);
    }
    @GetMapping("/my")
    public ResponseEntity<?> getMyBoards(@CurrentUser UserPrincipal currentUser) {
        User user = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new AppException("User not found"));
        Iterable<BoardLocalGroupUserLink> result = boardLocalGroupUserLinkRepository.findByUser(user);
        Set<BoardWidgetResponse> boards = new HashSet<>();
        result.forEach(item -> {
            Board board = item.getBoard();
            boards.add(new BoardWidgetResponse(board.getId(), board.getName(), board.getDescription()));
        });
        return new ResponseEntity<>(boards, HttpStatus.OK);
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<?> getBoard(@CurrentUser UserPrincipal currentUser,
                                      @PathVariable("boardId") Long boardId) {
        User user = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new AppException("User not found"));
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new AppException("Board not found"));
        if(userBoardPermissionsValidator.hasRequiredPerms(user, board, LocalRoleName.LOCAL_ROLE_VIEWER)) {
            BoardViewResponse response = boardToBoardViewResponseConverter.convert(board);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
    }

    @PostMapping("/{boardId}/addTask")
    @Transactional
    public ResponseEntity<?> addNewTask(@CurrentUser UserPrincipal currentUser,
                           @PathVariable("boardId") Long boardId,
                           @RequestBody TaskRequest taskRequest) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new AppException("Board not found"));
        User author = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new AppException("User not found"));
        if(userBoardPermissionsValidator.hasRequiredPerms(author, board, LocalRoleName.LOCAL_ROLE_USER)) {
            Task task = new Task(taskRequest.getName(), taskRequest.getDescription(), board, author);
            task.setState(taskStateRepository.findByName(TaskStateName.TASK_STATE_TO_DO)
                    .orElseThrow(() -> new AppException("Task state not found")));
            task.setType(taskTypeRepository.findByName(TaskTypeName.TASK_TYPE_OPEN)
                    .orElseThrow(() -> new AppException("Task type not found")));
            task.setPriority(taskPriorityRepository.findByName(TaskPriorityName.TASK_PRIORITY_NORMAL)
                    .orElseThrow(() -> new AppException("Priority not found")));
            taskRepository.save(task);
            } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>("Task created!", HttpStatus.CREATED);
    }

}
