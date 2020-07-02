package com.taskboard.security;

import com.taskboard.model.BoardLocalGroupUserLink;
import com.taskboard.model.User;
import com.taskboard.repository.BoardLocalGroupUserLinkRepository;
import com.taskboard.repository.UserRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class UserInvitationSecurityValidator {
    final
    UserRepository userRepository;
    final
    BoardLocalGroupUserLinkRepository boardLocalGroupUserLinkRepository;

    public UserInvitationSecurityValidator(BoardLocalGroupUserLinkRepository boardLocalGroupUserLinkRepository, UserRepository userRepository) {
        this.boardLocalGroupUserLinkRepository = boardLocalGroupUserLinkRepository;
        this.userRepository = userRepository;
    }

    public boolean validate(Long invitationId, Long requestingUserId) throws NotFoundException {
        BoardLocalGroupUserLink invitation = boardLocalGroupUserLinkRepository.findById(invitationId)
                .orElseThrow(() -> new NotFoundException("Invitation not found"));
        return invitation.getUser().getId().equals(requestingUserId);
    }
}
