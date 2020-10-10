package com.taskboard.controller;

import com.taskboard.model.User;
import com.taskboard.payload.BoardUserResponse;
import com.taskboard.payload.UserResponse;
import com.taskboard.payloadConverter.UserMapper;
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
    UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    //not needed to be secured
    public ResponseEntity<?> getMyUser(@CurrentUser UserPrincipal currentUser) {
        try {
            User user = userService.findUserById(currentUser.getId());
            UserResponse res = UserMapper.userToUserResponse(user);
            return new ResponseEntity<>(res, HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }



}
