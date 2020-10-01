package com.taskboard.service;

import com.taskboard.exception.BadRequestException;
import com.taskboard.model.Board;
import com.taskboard.model.BoardLocalGroupUserLink;
import com.taskboard.model.LocalRole;
import com.taskboard.model.User;
import com.taskboard.payload.InvitationResponse;
import com.taskboard.repository.BoardLocalGroupUserLinkRepository;
import com.taskboard.repository.BoardRepository;
import com.taskboard.repository.LocalRoleRepository;
import com.taskboard.repository.UserRepository;
import javassist.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InvitationService {
    final
    UserRepository userRepository;
    final
    BoardRepository boardRepository;
    final
    LocalRoleRepository localRoleRepository;
    final
    BoardLocalGroupUserLinkRepository boardLocalGroupUserLinkRepository;

    InvitationService(LocalRoleRepository localRoleRepository, BoardLocalGroupUserLinkRepository boardLocalGroupUserLinkRepository, UserRepository userRepository, BoardRepository boardRepository) {
        this.localRoleRepository = localRoleRepository;
        this.boardLocalGroupUserLinkRepository = boardLocalGroupUserLinkRepository;
        this.userRepository = userRepository;
        this.boardRepository = boardRepository;
    }

    public void inviteUserToBoard(Long boardId, Long localRoleId, String usernameOrEmail) throws Exception {
        User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> new NotFoundException("User not found"));

        boolean present = boardLocalGroupUserLinkRepository
                .findFirstByUserIdAndBoardIdOrderByLocalRoleDesc(user.getId(), boardId)
                .isPresent();
        if(present) throw new BadRequestException("User already invited");

        Board board = boardRepository.findBoardById(boardId)
                .orElseThrow(() -> new NotFoundException("Board not found"));
        LocalRole role = localRoleRepository.findById(localRoleId)
                .orElseThrow(() -> new NotFoundException("Local role not found"));

        BoardLocalGroupUserLink boardLocalGroupUserLink = new BoardLocalGroupUserLink(
                board, role, user, false);
        boardLocalGroupUserLinkRepository.save(boardLocalGroupUserLink);
    }

    public List<InvitationResponse> getUsersInvitations(Long userId) throws NotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
        List<BoardLocalGroupUserLink> boardGroupUserLinks
                = boardLocalGroupUserLinkRepository.findAllByUserAndAcceptedIsFalse(user);
        return boardGroupUserLinks.stream().map(link -> new InvitationResponse(
                link.getId(), link.getBoard().getName(), link.getBoard().getDescription()))
                .collect(Collectors.toList());
    }

    public void acceptInvitation(Long invitationId) throws NotFoundException {
        BoardLocalGroupUserLink invitation = boardLocalGroupUserLinkRepository.findById(invitationId)
                .orElseThrow(() -> new NotFoundException("Invitation doesn't exist"));
        invitation.setAccepted(true);
        boardLocalGroupUserLinkRepository.save(invitation);
    }

    public void deleteInvitation(Long invitationId) throws NotFoundException {
        BoardLocalGroupUserLink invitation = boardLocalGroupUserLinkRepository.findById(invitationId)
                .orElseThrow(() -> new NotFoundException("Invitation doesn't exist"));
        boardLocalGroupUserLinkRepository.deleteById(invitationId);
    }
}
