package com.taskboard.controller;

import com.taskboard.payload.BoardLocalGroupUserLinkRequest;
import com.taskboard.payload.InvitationResponse;
import com.taskboard.security.CurrentUser;
import com.taskboard.security.UserPrincipal;
import com.taskboard.service.InvitationService;
import javassist.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@PreAuthorize("hasRole('USER')")
@RequestMapping("/api/invitation")
public class InvitationController {
    final
    InvitationService invitationService;

    public InvitationController(InvitationService invitationService) {
        this.invitationService = invitationService;
    }

    @GetMapping
    public ResponseEntity<?> getUsersInvitations(@CurrentUser UserPrincipal currentUser) {
        try {
            List<InvitationResponse> invitations = invitationService.getUsersInvitations(currentUser.getId());
            return new ResponseEntity<>(invitations, HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/{invitationId}")
    public ResponseEntity<?> acceptInvitation(@CurrentUser UserPrincipal currentUser, @PathVariable Long invitationId) {
        try {
            invitationService.acceptInvitation(invitationId);
            return new ResponseEntity<>("Invitation accepted", HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping()
    public ResponseEntity<?> inviteUserToBoard(
            @CurrentUser UserPrincipal currentUser, @RequestBody BoardLocalGroupUserLinkRequest link) {
        Long boardId = link.getBoardId();
        String usernameOrEmail = link.getUsernameOrEmail();
        Long roleId = link.getLocalRoleId();
        try {
            invitationService.inviteUserToBoard(boardId, roleId, usernameOrEmail);
            return new ResponseEntity<>("Invitation sent", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{invitationId}")
    public ResponseEntity<?> deleteInvitation(@CurrentUser UserPrincipal currentUser,
                                              @PathVariable Long invitationId) {
        try {
            invitationService.deleteInvitation(invitationId);
            return new ResponseEntity<>("Invitation deleted", HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
