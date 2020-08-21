package com.taskboard.controller;

import com.taskboard.model.Board;
import com.taskboard.payload.BoardDetailedViewResponse;
import com.taskboard.payload.BoardRequest;
import com.taskboard.payload.BoardUserResponse;
import com.taskboard.payload.BoardViewResponse;
import com.taskboard.payloadConverter.BoardMapper;
import com.taskboard.security.CurrentUser;
import com.taskboard.security.UserPrincipal;
import com.taskboard.service.BoardService;
import javassist.NotFoundException;
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

    @GetMapping("/{id}")
    public ResponseEntity<?> getBoardById(@CurrentUser UserPrincipal currentUser, @PathVariable("id") Long id) {
        try {
            Board board = boardService.getBoardById(id);
            BoardDetailedViewResponse res = BoardMapper.boardToBoardDetailedView(board);
            return new ResponseEntity<>(res, HttpStatus.OK);
        } catch (NotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{boardId}")
    public ResponseEntity<?> updateBoard(@PathVariable Long boardId,
                                         @CurrentUser UserPrincipal currentUser,
                                         @RequestBody BoardRequest boardRequest) {
        try {
            Board board = boardService.updateBoard(boardId, boardRequest);
            BoardViewResponse res = new BoardViewResponse(board.getId(), board.getName(), board.getDescription());
            return new ResponseEntity<>(res, HttpStatus.CREATED);
        }
        catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping
    public ResponseEntity<?> addNewBoard(@CurrentUser UserPrincipal currentUser, @RequestBody BoardRequest boardRequest) {
        try {
            Board board = boardService.addNewBoard(currentUser.getId(), boardRequest);
            BoardViewResponse res = new BoardViewResponse(board.getId(), board.getName(), board.getDescription());
            return new ResponseEntity<>(res, HttpStatus.CREATED);
        }
        catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("{boardId}/members") //all members
    public ResponseEntity<?> getBoardMembers(@CurrentUser UserPrincipal currentUser, @PathVariable Long boardId) {
        try {
            List<BoardUserResponse> boardMembers = boardService.getBoardMembers(boardId);
            return new ResponseEntity<>(boardMembers, HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("{boardId}/users") //members with at least USER role
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
