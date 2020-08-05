package com.taskboard.controller;

import com.taskboard.model.BoardLocalGroupUserLink;
import com.taskboard.model.LocalRole;
import com.taskboard.payload.BoardLocalGroupUserLinkRequest;
import com.taskboard.security.CurrentUser;
import com.taskboard.security.UserPrincipal;
import com.taskboard.service.LocalRoleService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@PreAuthorize("hasRole('USER')")
@RequestMapping("/api/local-role")
public class LocalRoleController {
    final
    LocalRoleService localRoleService;

    public LocalRoleController(LocalRoleService localRoleService) {
        this.localRoleService = localRoleService;
    }

    @GetMapping()
    public ResponseEntity<?> getAllRoles() {
        return new ResponseEntity<>(localRoleService.getAllRoles(), HttpStatus.OK);
    }

    @GetMapping("/my")
    public ResponseEntity<?> getLocalRole(@CurrentUser UserPrincipal currentUser, @RequestParam Long boardId) {
        try {
            LocalRole role = localRoleService.getUsersLocalRole(currentUser.getId(), boardId);
            return new ResponseEntity<>(role, HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


}