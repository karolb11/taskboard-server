package com.taskboard.service;

import com.taskboard.model.*;
import com.taskboard.payload.BoardRequest;
import com.taskboard.payload.BoardView;
import com.taskboard.payloadConverter.BoardMapper;
import com.taskboard.repository.BoardLocalGroupUserLinkRepository;
import com.taskboard.repository.BoardRepository;
import com.taskboard.repository.LocalRoleRepository;
import com.taskboard.repository.UserRepository;
import javassist.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class BoardService {
    final
    BoardLocalGroupUserLinkRepository boardLocalGroupUserLinkRepository;
    final
    BoardRepository boardRepository;
    final
    UserRepository userRepository;
    final
    BoardMapper boardMapper;
    final
    LocalRoleRepository localRoleRepository;
    final
    LocalRoleService localRoleService;

    public BoardService(BoardLocalGroupUserLinkRepository boardLocalGroupUserLinkRepository, BoardRepository boardRepository, UserRepository userRepository, BoardMapper boardMapper, LocalRoleRepository localRoleRepository, LocalRoleService localRoleService) {
        this.boardLocalGroupUserLinkRepository = boardLocalGroupUserLinkRepository;
        this.boardRepository = boardRepository;
        this.userRepository = userRepository;
        this.boardMapper = boardMapper;
        this.localRoleRepository = localRoleRepository;
        this.localRoleService = localRoleService;
    }

    public List<BoardView> getUsersBoards(Long userId) throws NotFoundException {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("wrong userId"));
        return getUsersBoards(user);
    }

    public List<BoardView> getUsersBoards(User user) {
        Iterable<BoardLocalGroupUserLink> boardLocalGroupUserLinkByUser= boardLocalGroupUserLinkRepository.findByUser(user);
        Set<Long> userBoardsIds = new HashSet<>();
        boardLocalGroupUserLinkByUser.forEach(item -> {
            userBoardsIds.add(item.getId());
        });
        return boardRepository.findBoardViewByIdIn(userBoardsIds);
    }

    @Transactional
    public Board addNewBoard(Long userId, BoardRequest boardRequest) throws NotFoundException {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("wrong userId"));
        return addNewBoard(user, boardRequest);
    }

    @Transactional
    public Board addNewBoard(User user, BoardRequest boardRequest) throws NotFoundException {
        Board board = new Board(boardRequest.getName(), boardRequest.getDescription());
        board = boardRepository.save(board);
        localRoleService.grantRoleToUser(board, user, LocalRoleName.LOCAL_ROLE_OWNER);
        return board;
    }
}
