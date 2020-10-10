package com.taskboard.controller;

import com.taskboard.model.Board;
import com.taskboard.payload.*;
import com.taskboard.payloadConverter.BoardMapper;
import com.taskboard.security.CurrentUser;
import com.taskboard.security.UserPrincipal;
import com.taskboard.service.BoardService;
import javassist.NotFoundException;
import org.springframework.context.annotation.Role;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;


@RestController
@RequestMapping("api/board")
@PreAuthorize("hasRole('USER')")
public class BoardController {

    final
    BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @GetMapping
    public ResponseEntity<?> getUserBoards(@CurrentUser UserPrincipal userPrincipal) {
        try {
            List<BoardViewResponse> usersBoards = boardService.getUsersBoards(userPrincipal.getId());
            return new ResponseEntity<>(usersBoards, HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{boardId}")
    @PreAuthorize("hasAuthority('board'+#boardId+':'+'LOCAL_ROLE_VIEWER')" +
            "or hasAuthority('board'+#boardId+':'+'LOCAL_ROLE_USER')" +
            "or hasAuthority('board'+#boardId+':'+'LOCAL_ROLE_OWNER')")
    public ResponseEntity<?> getBoardById(@PathVariable("boardId") Long boardId) {
        try {
            Board board = boardService.getBoardById(boardId);
            BoardDetailedViewResponse res = BoardMapper.boardToBoardDetailedView(board);
            return new ResponseEntity<>(res, HttpStatus.OK);
        } catch (NotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{boardId}")
    @PreAuthorize("hasAuthority('board'+#boardId+':'+'LOCAL_ROLE_OWNER')")
    public ResponseEntity<?> updateBoard(@PathVariable Long boardId,
                                         @CurrentUser UserPrincipal currentUser,
                                         @RequestBody BoardRequest boardRequest) {
        try {
            boardService.updateBoard(boardId, boardRequest);
            ApiResponse res = new ApiResponse(true, "Board setting saved");
            return new ResponseEntity<>(res, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping
    public ResponseEntity<?> addNewBoard(@CurrentUser UserPrincipal currentUser, @RequestBody BoardRequest boardRequest) {
        try {
            Board board = boardService.addNewBoard(currentUser.getId(), boardRequest);
            BoardViewResponse res = new BoardViewResponse(board.getId(), board.getName(), board.getDescription());
            return new ResponseEntity<>(res, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("{boardId}/members") //all members
    @PreAuthorize("hasAuthority('board'+#boardId+':'+'LOCAL_ROLE_VIEWER')" +
            "or hasAuthority('board'+#boardId+':'+'LOCAL_ROLE_USER')" +
            "or hasAuthority('board'+#boardId+':'+'LOCAL_ROLE_OWNER')")
    public ResponseEntity<?> getBoardMembers(@CurrentUser UserPrincipal currentUser,
                                             @PathVariable("boardId") Long boardId) {
        try {
            List<BoardUserResponse> boardMembers = boardService.getBoardMembers(boardId);
            return new ResponseEntity<>(boardMembers, HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("{boardId}/users") //members with at least USER role
    @PreAuthorize("hasAuthority('board'+#boardId+':'+'LOCAL_ROLE_VIEWER')" +
            "or hasAuthority('board'+#boardId+':'+'LOCAL_ROLE_USER')" +
            "or hasAuthority('board'+#boardId+':'+'LOCAL_ROLE_OWNER')")
    public ResponseEntity<?> getBoardUsers(
            @CurrentUser UserPrincipal userPrincipal,
            @PathVariable Long boardId) {
        try {
            Set<BoardUserResponse> boardUsers = boardService.getBoardUsers(boardId);
            return new ResponseEntity<>(boardUsers, HttpStatus.OK);

        } catch (NotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
