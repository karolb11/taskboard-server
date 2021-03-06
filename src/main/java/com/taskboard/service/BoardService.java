package com.taskboard.service;

import com.taskboard.model.*;
import com.taskboard.payload.BoardRequest;
import com.taskboard.payload.BoardUserResponse;
import com.taskboard.payload.BoardViewResponse;
import com.taskboard.payloadConverter.BoardMapper;
import com.taskboard.repository.BoardLocalGroupUserLinkRepository;
import com.taskboard.repository.BoardRepository;
import com.taskboard.repository.LocalRoleRepository;
import com.taskboard.repository.UserRepository;
import javassist.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    public List<BoardViewResponse> getAllBoards() {
        return boardRepository.findAll().stream()
                .filter(board -> !board.isArchived())
                .map(BoardMapper::boardToBoardViewResponse)
                .collect(Collectors.toList());
    }

    public List<BoardViewResponse> getUsersBoards(Long userId) throws NotFoundException {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("wrong userId"));
        return getUsersBoards(user);
    }

    public List<BoardViewResponse> getUsersBoards(User user) {
        List<BoardLocalGroupUserLink> boardLocalGroupUserLinkByUser =
                boardLocalGroupUserLinkRepository.findByUserAndAcceptedIsTrue(user);
        Set<Long> boardIds = boardLocalGroupUserLinkByUser
                .stream()
                .map(link -> link.getBoard().getId())
                .collect(Collectors.toSet());
        return boardRepository.findBoardViewByIdInAndArchivedIsFalse(boardIds);
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
        localRoleService.grantLocalRoleToUser(board, user, LocalRoleName.LOCAL_ROLE_OWNER);
        return board;
    }

    @Transactional
    public Board updateBoard(Long boardId, BoardRequest boardRequest) throws NotFoundException {
        Board board = boardRepository.findBoardById(boardId)
                .orElseThrow(() -> new NotFoundException("Board not found"));
        board.setName(boardRequest.getName());
        board.setDescription(boardRequest.getDescription());
        return boardRepository.save(board);

    }

    public Board getBoardById(Long boardId) throws NotFoundException {
        return boardRepository.findBoardById(boardId)
                .orElseThrow(() -> new NotFoundException("Board not found"));
    }

    public List<BoardUserResponse> getBoardMembers(Long boardId) throws NotFoundException {
        List<BoardLocalGroupUserLink> links = boardLocalGroupUserLinkRepository
                .findByBoardId(boardId);
        return links.stream().map(link ->
                new BoardUserResponse(link.getId(), link.getUser().getId(), link.getUser().getName(), link.getLocalRole(), link.isAccepted()))
                .collect(Collectors.toList());
    }
    public Set<BoardUserResponse> getBoardUsers(Long boardId) throws NotFoundException {
        return getBoardMembersWithRole(boardId, localRoleService.findRoleByName(LocalRoleName.LOCAL_ROLE_USER));
    }

    public Set<BoardUserResponse> getBoardMembersWithRole(Long boardId, LocalRole localRole) throws NotFoundException {
        Set<BoardLocalGroupUserLink> boardUsers = boardLocalGroupUserLinkRepository
                .findByBoardIdAndLocalRoleGreaterThanEqualAndAcceptedIsTrue(boardId, localRole);
        Set<BoardUserResponse> users = boardUsers
                .stream()
                .map(i -> new BoardUserResponse(i.getUser().getId(), i.getUser().getName()))
                .collect(Collectors.toSet());
        return users;
    }


    public void archiveBoard(Long id) throws NotFoundException {
        Board board = boardRepository.findBoardById(id)
                .orElseThrow(() -> new NotFoundException("Board not found"));
        board.setArchived(true);
        boardRepository.save(board);
    }
}
