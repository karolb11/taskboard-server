package com.taskboard.controller;

import com.taskboard.exception.AppException;
import com.taskboard.exception.EmailTakenException;
import com.taskboard.exception.UsernameTakenException;
import com.taskboard.model.Role;
import com.taskboard.model.RoleName;
import com.taskboard.model.User;
import com.taskboard.payload.ApiResponse;
import com.taskboard.payload.JwtAuthenticationResponse;
import com.taskboard.payload.LoginRequest;
import com.taskboard.payload.SignUpRequest;
import com.taskboard.repository.RoleRepository;
import com.taskboard.repository.UserRepository;
import com.taskboard.security.JwtTokenProvider;
import com.taskboard.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import javax.xml.transform.OutputKeys;
import java.net.URI;
import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {
    final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        try {
            authService.registerUser(signUpRequest);
            ApiResponse res = new ApiResponse(true, "Account created");
            return new ResponseEntity<>(res, HttpStatus.OK);
        } catch (UsernameTakenException | EmailTakenException e) {
            ApiResponse res = new ApiResponse(false, e.getMessage());
            return new ResponseEntity<>(res, HttpStatus.CONFLICT);
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(
            @Valid @RequestBody LoginRequest loginRequest) {
        String jwt = authService.authenticateUserAndGenerateJwt(loginRequest);
        JwtAuthenticationResponse res = new JwtAuthenticationResponse(jwt);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}
