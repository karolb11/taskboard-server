package com.taskboard.aspect;

import com.taskboard.model.User;
import com.taskboard.payload.UpdateUserRequest;
import com.taskboard.repository.UserRepository;
import com.taskboard.security.UserPrincipal;
import javassist.NotFoundException;
import lombok.AllArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.xml.bind.ValidationException;

@Aspect
@AllArgsConstructor
@Configuration
public class UpdateUserConfirmPasswordValidator {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Around("execution(* com.taskboard.controller.UserController.updateUser(..)) && args(currentUser, userId, updateUserRequest)")
    public ResponseEntity<?> validateConfirmingPassword(ProceedingJoinPoint joinPoint,
                                                        UserPrincipal currentUser,
                                                        Long userId,
                                                        UpdateUserRequest updateUserRequest)
            throws Throwable {
        if(!isSensitiveType(updateUserRequest))
            return (ResponseEntity<?>) joinPoint.proceed();

        User requester = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new NotFoundException("user not found"));
        boolean matches = passwordEncoder.matches(updateUserRequest.getCurrentPassword(), requester.getPassword());
        if(!matches)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        else return (ResponseEntity<?>) joinPoint.proceed();
    }

    private boolean isSensitiveType(UpdateUserRequest updateUserRequest) {
        return updateUserRequest.getType() == UpdateUserRequest.UpdateType.CHANGE_PASSWORD ||
                updateUserRequest.getType() == UpdateUserRequest.UpdateType.DEACTIVATE_ACCOUNT;
    }
}
