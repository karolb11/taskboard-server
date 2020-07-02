package com.taskboard.service;

import com.taskboard.model.Board;
import com.taskboard.model.BoardLocalGroupUserLink;
import com.taskboard.model.LocalRole;
import com.taskboard.model.LocalRoleName;
import com.taskboard.payload.BoardUserResponse;
import com.taskboard.repository.BoardLocalGroupUserLinkRepository;
import com.taskboard.repository.BoardRepository;
import com.taskboard.security.UserBoardPermissionsValidator;
import javassist.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    final
    BoardLocalGroupUserLinkRepository boardLocalGroupUserLinkRepository;
    final
    BoardRepository boardRepository;
    final
    LocalRoleService localRoleService;
    final
    UserBoardPermissionsValidator userBoardPermissionsValidator;

    public UserService(BoardLocalGroupUserLinkRepository boardLocalGroupUserLinkRepository, BoardRepository boardRepository, LocalRoleService localRoleService, UserBoardPermissionsValidator userBoardPermissionsValidator) {
        this.boardLocalGroupUserLinkRepository = boardLocalGroupUserLinkRepository;
        this.boardRepository = boardRepository;
        this.localRoleService = localRoleService;
        this.userBoardPermissionsValidator = userBoardPermissionsValidator;
    }




}
