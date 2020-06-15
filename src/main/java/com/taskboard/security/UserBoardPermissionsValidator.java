package com.taskboard.security;

import com.taskboard.model.*;
import com.taskboard.repository.BoardLocalGroupUserLinkRepository;
import com.taskboard.repository.BoardRepository;
import com.taskboard.repository.LocalRoleRepository;
import com.taskboard.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class UserBoardPermissionsValidator {
    @Autowired
    BoardLocalGroupUserLinkRepository boardLocalGroupUserLinkRepository;

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    LocalRoleRepository localRoleRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    LocalRoleHierarchy localRoleHierarchy;


    public boolean hasRequiredPerms(User user, Board board, LocalRoleName permissionLevel){
        Iterable<BoardLocalGroupUserLink> boardLocalGroupUserLinks =
                boardLocalGroupUserLinkRepository.findByUserAndBoard(user, board);
        for(BoardLocalGroupUserLink i: boardLocalGroupUserLinks) {
            if(localRoleHierarchy.isContaining(i.getLocalRole().getName(), permissionLevel)) return true;
        }
        return false;
    }

}
