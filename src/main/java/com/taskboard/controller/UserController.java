package com.taskboard.controller;

import com.taskboard.model.User;
import com.taskboard.repository.UserRepository;
import com.taskboard.security.CurrentUser;
import com.taskboard.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@PreAuthorize("hasRole('USER')")
@RequestMapping("/api")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @GetMapping("/me")
    public Optional<User> getMyUser(@CurrentUser UserPrincipal currentUser) {
        return userRepository.findById(currentUser.getId());
    }

}
