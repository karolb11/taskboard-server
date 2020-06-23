package com.taskboard.service;

import com.taskboard.model.*;
import com.taskboard.repository.BoardLocalGroupUserLinkRepository;
import com.taskboard.repository.LocalRoleRepository;
import javassist.NotFoundException;
import org.springframework.stereotype.Service;

@Service

public class LocalRoleService {
    final
    LocalRoleRepository localRoleRepository;
    final
    BoardLocalGroupUserLinkRepository boardLocalGroupUserLinkRepository;

    LocalRoleService(LocalRoleRepository localRoleRepository, BoardLocalGroupUserLinkRepository boardLocalGroupUserLinkRepository) {
        this.localRoleRepository = localRoleRepository;
        this.boardLocalGroupUserLinkRepository = boardLocalGroupUserLinkRepository;
    }

    public void grantRoleToUser(Board board, User user, LocalRoleName roleName) throws NotFoundException {
        LocalRole role = findRole(roleName);
        BoardLocalGroupUserLink boardLocalGroupUserLink =
                new BoardLocalGroupUserLink(board, role, user);
        boardLocalGroupUserLinkRepository.save(boardLocalGroupUserLink);
    }

    LocalRole findRole(LocalRoleName roleName) throws NotFoundException {
        return localRoleRepository.findByName(roleName)
                .orElseThrow(() -> new NotFoundException("role not found"));
    }


}
