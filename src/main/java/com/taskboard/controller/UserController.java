package com.taskboard.controller;

import com.taskboard.model.User;
import com.taskboard.payload.BoardUserResponse;
import com.taskboard.repository.UserRepository;
import com.taskboard.security.CurrentUser;
import com.taskboard.security.UserPrincipal;
import com.taskboard.service.UserService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Set;

@RestController
@PreAuthorize("hasRole('USER')")
@RequestMapping("/api/user")
public class UserController {

    final
    UserRepository userRepository;
    final
    UserService userService;

    public UserController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @GetMapping("/me")
    public Optional<User> getMyUser(@CurrentUser UserPrincipal currentUser) {
        return userRepository.findById(currentUser.getId());
    }



}
