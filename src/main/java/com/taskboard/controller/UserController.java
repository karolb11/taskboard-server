package com.taskboard.controller;

import com.taskboard.exception.EmailTakenException;
import com.taskboard.model.User;
import com.taskboard.payload.*;
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

import javax.xml.bind.ValidationException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping("/api/user")
public class UserController {

    //TODO: deactivate account
    final
    UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    @PreAuthorize("hasAuthority('ROLE_MOD') or hasAuthority('ROLE_ADMIN')")
    //not needed to be secured
    public ResponseEntity<?> findAll() {
        List<UserResponse> res = userService.findAll();
        return new ResponseEntity<>(res, HttpStatus.OK);
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

    @PatchMapping("/{userId}")
    public ResponseEntity<?> updateUser(@CurrentUser UserPrincipal currentUser,
                                        @PathVariable Long userId,
                                        @RequestBody UpdateUserRequest updateUserRequest) {
        try {
            userService.updateUser(userId, updateUserRequest);
            return new ResponseEntity<>(new ApiResponse(true, "account updated"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/{userId}/role")
    @PreAuthorize("hasAuthority('ROLE_MOD') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> updateRole(@CurrentUser UserPrincipal currentUser,
                                        @PathVariable Long userId,
                                        @RequestBody Long roleId) {
        try {
            userService.updateUserRole(userId, roleId);
            return new ResponseEntity<>(new ApiResponse(true, "account updated"), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/{userId}/deactivate")
    @PreAuthorize("hasAuthority('ROLE_MOD') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> deactivate(@CurrentUser UserPrincipal currentUser,
                                        @PathVariable Long userId) {
        try {
            userService.deactivateUser(userId);
            return new ResponseEntity<>(new ApiResponse(true, "account updated"), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


}
