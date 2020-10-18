package com.taskboard.service;

import com.taskboard.exception.AppException;
import com.taskboard.exception.EmailTakenException;
import com.taskboard.exception.UsernameTakenException;
import com.taskboard.model.Role;
import com.taskboard.model.RoleName;
import com.taskboard.model.User;
import com.taskboard.payload.LoginRequest;
import com.taskboard.payload.SignUpRequest;
import com.taskboard.repository.RoleRepository;
import com.taskboard.repository.UserRepository;
import com.taskboard.security.JwtTokenProvider;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;


@Service
@AllArgsConstructor
public class AuthService {
    final AuthenticationManager authenticationManager;
    final UserRepository userRepository;
    final RoleRepository roleRepository;
    final PasswordEncoder passwordEncoder;
    final JwtTokenProvider tokenProvider;

    public String authenticateUserAndGenerateJwt(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsernameOrEmail(),
                        loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return tokenProvider.generateToken(authentication);
    }

    public void registerUser(SignUpRequest signUpRequest)
            throws UsernameTakenException, EmailTakenException {
        if(userRepository.existsByUsername(signUpRequest.getUsername())) {
            throw new UsernameTakenException();
        }
        if(userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new EmailTakenException();
        }
        createUserAndSaveInRepo(signUpRequest);
    }

    @Transactional
    void createUserAndSaveInRepo(SignUpRequest signUpRequest) {
        User user = new User(signUpRequest.getName(), signUpRequest.getUsername(),
                signUpRequest.getEmail(), signUpRequest.getPassword());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new AppException("User Role not set."));
        user.setRoles(Collections.singleton(userRole));
        userRepository.save(user);
    }
}
