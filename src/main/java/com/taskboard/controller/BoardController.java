package com.taskboard.controller;

import com.taskboard.model.Board;
import com.taskboard.payload.BoardRequest;
import com.taskboard.payload.BoardView;
import com.taskboard.payload.BoardViewResponse;
import com.taskboard.security.CurrentUser;
import com.taskboard.security.UserPrincipal;
import com.taskboard.service.BoardService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("api/board")
public class BoardController {

    final
    BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @GetMapping
    public ResponseEntity<?> getUsersBoards(@CurrentUser UserPrincipal userPrincipal) {
        try {
            List<BoardView> usersBoards = boardService.getUsersBoards(userPrincipal.getId());
            return new ResponseEntity<>(usersBoards, HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBoardById(@CurrentUser UserPrincipal userPrincipal) {
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping
    public ResponseEntity<?> addNewBoard(@CurrentUser UserPrincipal userPrincipal, @RequestBody BoardRequest boardRequest) {
        try {
            Board board = boardService.addNewBoard(userPrincipal.getId(), boardRequest);
            BoardViewResponse res = new BoardViewResponse(board.getId(), board.getName(), board.getDescription());
            return new ResponseEntity<>(res, HttpStatus.CREATED);
        }
        catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
