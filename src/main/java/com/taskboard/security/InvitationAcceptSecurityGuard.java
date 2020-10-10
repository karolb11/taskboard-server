package com.taskboard.security;

import com.taskboard.model.BoardLocalGroupUserLink;
import com.taskboard.repository.BoardLocalGroupUserLinkRepository;
import com.taskboard.repository.UserRepository;
import javassist.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class InvitationAcceptSecurityGuard {
    private final UserRepository userRepository;
    private final BoardLocalGroupUserLinkRepository boardLocalGroupUserLinkRepository;

    public boolean validate(Long invitationId, Long requesterId) throws NotFoundException {
        BoardLocalGroupUserLink invitation = boardLocalGroupUserLinkRepository.findById(invitationId)
                .orElseThrow(() -> new NotFoundException("Invitation not found"));
        return invitation.getUser().getId().equals(requesterId);
    }
}
