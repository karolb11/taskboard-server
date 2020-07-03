package com.taskboard.security;

import com.taskboard.model.*;
import com.taskboard.repository.BoardLocalGroupUserLinkRepository;
import com.taskboard.repository.BoardRepository;
import com.taskboard.repository.LocalRoleRepository;
import com.taskboard.repository.UserRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class UserBoardPermissionsValidator {
    final
    BoardLocalGroupUserLinkRepository boardLocalGroupUserLinkRepository;

    final
    BoardRepository boardRepository;

    final
    LocalRoleRepository localRoleRepository;

    final
    UserRepository userRepository;

    final
    LocalRoleHierarchy localRoleHierarchy;

    public UserBoardPermissionsValidator(BoardLocalGroupUserLinkRepository boardLocalGroupUserLinkRepository, BoardRepository boardRepository, LocalRoleRepository localRoleRepository, UserRepository userRepository, LocalRoleHierarchy localRoleHierarchy) {
        this.boardLocalGroupUserLinkRepository = boardLocalGroupUserLinkRepository;
        this.boardRepository = boardRepository;
        this.localRoleRepository = localRoleRepository;
        this.userRepository = userRepository;
        this.localRoleHierarchy = localRoleHierarchy;
    }


    public boolean validate(User user, Board board, LocalRoleName permissionLevel){
        Iterable<BoardLocalGroupUserLink> boardLocalGroupUserLinks =
                boardLocalGroupUserLinkRepository.findByUserAndBoardAndAcceptedIsTrue(user, board);
        for(BoardLocalGroupUserLink i: boardLocalGroupUserLinks) {
            if(localRoleHierarchy.isContaining(i.getLocalRole().getName(), permissionLevel)) return true;
        }
        return false;
    }
    public boolean validate(Long userId, Long boardId, LocalRoleName permissionLevel) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new NotFoundException("user not found"));
            Board board = boardRepository.findBoardById(boardId)
                    .orElseThrow(() -> new NotFoundException("board not found"));
            return validate(user, board, permissionLevel);
        }catch (NotFoundException e) {
            return false;
        }
    }

}
