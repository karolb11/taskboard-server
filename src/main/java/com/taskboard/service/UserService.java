package com.taskboard.service;

import com.taskboard.exception.EmailTakenException;
import com.taskboard.model.Role;
import com.taskboard.model.User;
import com.taskboard.payload.UpdateUserRequest;
import com.taskboard.payload.UserResponse;
import com.taskboard.payloadConverter.UserMapper;
import com.taskboard.repository.RoleRepository;
import com.taskboard.repository.UserRepository;
import javassist.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.xml.bind.ValidationException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public User findUserById(Long userId) throws NotFoundException {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    public void updateUser(Long userId, UpdateUserRequest updateUserRequest)
            throws NotFoundException, EmailTakenException, ValidationException {
        User user = findUserById(userId);
        switch (updateUserRequest.getType()) {
            case UPDATE_DATA: updateData(user, updateUserRequest); break;
            case CHANGE_PASSWORD: changePassword(user, updateUserRequest); break;
            case DEACTIVATE_ACCOUNT: deactivateAccount(user); break;
        }
    }

    private void updateData(User user, UpdateUserRequest updateUserRequest) throws EmailTakenException {
        if(isEmailTaken(user, updateUserRequest.getEmail()))
            throw new EmailTakenException();
        user.setName(updateUserRequest.getName());
        user.setEmail(updateUserRequest.getEmail());
        userRepository.save(user);
    }

    private void changePassword(User user, UpdateUserRequest updateUserRequest) throws ValidationException {
        if(updateUserRequest.getNewPassword().equals(updateUserRequest.getConfirmNewPassword()))
            throw new ValidationException("passwords doesn't match");
        String newPasswordEncoded = passwordEncoder.encode(updateUserRequest.getNewPassword());
        user.setPassword(newPasswordEncoded);
        userRepository.save(user);
    }

    private void deactivateAccount(User user) {
        user.setEnabled(false);
        userRepository.save(user);
    }

    private boolean isEmailTaken(User user, String email) {
        if(user.getEmail().equals(email))
            return false;
        else
            return userRepository.findByEmail(email).isPresent();
    }

    public List<UserResponse> findAll() {
        return userRepository.findAll().stream()
                .filter(User::isEnabled)
                .map(UserMapper::userToUserResponse)
                .collect(Collectors.toList());
    }

    public void updateUserRole(Long userId, Long roleId) throws NotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("user not found"));
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new NotFoundException("role not found"));
        Set<Role> roleSet = new HashSet<>();
        roleSet.add(role);
        user.setRoles(roleSet);
        userRepository.save(user);
    }

    public void deactivateUser(Long userId) throws NotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("user not found"));
        user.setEnabled(false);
        userRepository.save(user);
    }
}
