package com.taskboard.controller;

import com.taskboard.payload.ApiResponse;
import com.taskboard.payload.BoardLocalGroupUserLinkRequest;
import com.taskboard.payload.InvitationResponse;
import com.taskboard.security.CurrentUser;
import com.taskboard.security.InvitationAcceptSecurityGuard;
import com.taskboard.security.UserPrincipal;
import com.taskboard.service.InvitationService;
import com.taskboard.service.LocalRoleService;
import com.taskboard.utils.ResourceIdUtils;
import javassist.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping("/api/invitation")
@AllArgsConstructor
public class InvitationController {
    final InvitationService invitationService;
    final LocalRoleService localRoleService;
    final ResourceIdUtils resourceIdUtils;
    final InvitationAcceptSecurityGuard invitationAcceptSecurityGuard;

    @GetMapping
    public ResponseEntity<?> getUsersInvitations(@CurrentUser UserPrincipal currentUser) {
        try {
            List<InvitationResponse> invitations = invitationService.getUsersInvitations(currentUser.getId());
            return new ResponseEntity<>(invitations, HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping()
    @PreAuthorize("hasAuthority('board'+#link.getBoardId()+':'+'LOCAL_ROLE_OWNER')")
    public ResponseEntity<?> inviteUserToBoard(
            @RequestBody BoardLocalGroupUserLinkRequest link) {
        Long boardId = link.getBoardId();
        String usernameOrEmail = link.getUsernameOrEmail();
        Long roleId = link.getLocalRoleId();
        try {
            invitationService.inviteUserToBoard(boardId, roleId, usernameOrEmail);
            ApiResponse res = new ApiResponse(true, "Invitation sent");
            return new ResponseEntity<>(res, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{invitationId}")
    @PreAuthorize("hasAuthority('board'+@resourceIdUtils.getBoardIdByInvitationId(#invitationId)+':'+'LOCAL_ROLE_OWNER')")
    public ResponseEntity<?> editUsersLocalRole(@PathVariable Long invitationId,
                                                @PathParam("localRoleId") Long localRoleId) {
        try {
            localRoleService.setUsersLocalRole(invitationId, localRoleId);
            ApiResponse res = new ApiResponse(true, "Role modified");
            return new ResponseEntity<>(res, HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/{invitationId}")
    @PreAuthorize("@invitationAcceptSecurityGuard.validate(#invitationId, #currentUser.getId())")
    public ResponseEntity<?> acceptInvitation(@CurrentUser UserPrincipal currentUser, @PathVariable Long invitationId) {
        try {
            invitationService.acceptInvitation(invitationId);
            ApiResponse res = new ApiResponse(true, "invitation accepted");
            return new ResponseEntity<>(res, HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{invitationId}")
    @PreAuthorize("hasAuthority('board'+@resourceIdUtils.getBoardIdByInvitationId(#invitationId)+':'+'LOCAL_ROLE_OWNER')" +
        "or @invitationAcceptSecurityGuard.validate(#invitationId, #currentUser.getId())")
    public ResponseEntity<?> deleteInvitation(@PathVariable Long invitationId) {
        try {
            invitationService.deleteInvitation(invitationId);
            ApiResponse res = new ApiResponse(true, "invitation deleted");
            return new ResponseEntity<>(res, HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
