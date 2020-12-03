package com.taskboard.controller;

import com.taskboard.model.Role;
import com.taskboard.service.GlobalRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping("/api/global-role")
@RequiredArgsConstructor
public class GlobalRole {
    private final GlobalRoleService globalRoleService;

    @GetMapping
    ResponseEntity<?> findAll() {
        List<Role> res = globalRoleService.findAll();
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}
