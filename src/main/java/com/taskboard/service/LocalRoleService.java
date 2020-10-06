package com.taskboard.service;

import com.taskboard.model.*;
import com.taskboard.repository.BoardLocalGroupUserLinkRepository;
import com.taskboard.repository.BoardRepository;
import com.taskboard.repository.LocalRoleRepository;
import com.taskboard.repository.UserRepository;
import javassist.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class LocalRoleService {
    final
    UserRepository userRepository;
    final
    BoardRepository boardRepository;
    final
    LocalRoleRepository localRoleRepository;
    final
    BoardLocalGroupUserLinkRepository boardLocalGroupUserLinkRepository;

    LocalRoleService(LocalRoleRepository localRoleRepository, BoardLocalGroupUserLinkRepository boardLocalGroupUserLinkRepository, UserRepository userRepository, BoardRepository boardRepository) {
        this.localRoleRepository = localRoleRepository;
        this.boardLocalGroupUserLinkRepository = boardLocalGroupUserLinkRepository;
        this.userRepository = userRepository;
        this.boardRepository = boardRepository;
    }

    public List<LocalRole> getAllRoles() {
        return localRoleRepository.findAllByOrderByIdAsc();
    }

    public LocalRole getUsersLocalRole(Long userId, Long boardId) throws NotFoundException {
        BoardLocalGroupUserLink boardLocalGroupUserLink = boardLocalGroupUserLinkRepository
                .findFirstByUserIdAndBoardIdOrderByLocalRoleDesc(userId, boardId)
                .orElseThrow(() -> new NotFoundException("Not found"));
        return boardLocalGroupUserLink.getLocalRole();
    }

    public void grantLocalRoleToUser(Board board, User user, LocalRoleName roleName) throws NotFoundException {
        LocalRole role = findRoleByName(roleName);
        BoardLocalGroupUserLink boardLocalGroupUserLink =
                new BoardLocalGroupUserLink(board, role, user, true);
        boardLocalGroupUserLinkRepository.save(boardLocalGroupUserLink);
    }

    public void setUsersLocalRole(Long boardLocalGroupUserLinkId, Long localRoleId) throws NotFoundException {
        BoardLocalGroupUserLink boardLocalGroupUserLink =
                boardLocalGroupUserLinkRepository.findById(boardLocalGroupUserLinkId)
                .orElseThrow(() -> new NotFoundException("Not found"));
        LocalRole role = localRoleRepository.findById(localRoleId)
                .orElseThrow(() -> new NotFoundException("Not found"));

        boardLocalGroupUserLink.setLocalRole(role);
        boardLocalGroupUserLinkRepository.save(boardLocalGroupUserLink);
    }

    LocalRole findRoleByName(LocalRoleName roleName) throws NotFoundException {
        return localRoleRepository.findByName(roleName)
                .orElseThrow(() -> new NotFoundException("role not found"));
    }


}
