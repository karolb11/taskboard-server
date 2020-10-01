package com.taskboard.security;

import com.taskboard.model.LocalRoleName;
import com.taskboard.payload.CreateTaskRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Aspect
@Configuration
public class UserBoardSecurityAdvice {
    final
    UserBoardPermissionsValidator userBoardPermissionsValidator;

    public UserBoardSecurityAdvice(UserBoardPermissionsValidator userBoardPermissionsValidator) {
        this.userBoardPermissionsValidator = userBoardPermissionsValidator;
    }

    @Around("execution(* com.taskboard.controller.BoardController.getBoardById(..)) &&args(currentUser, boardId)")
    public ResponseEntity<?> validateUserPermsToBoardView(
            ProceedingJoinPoint joinPoint,
            UserPrincipal currentUser,
            Long boardId) throws Throwable {
        if(userBoardPermissionsValidator.validate(currentUser.getId(), boardId, LocalRoleName.LOCAL_ROLE_VIEWER))
            return (ResponseEntity<?>) joinPoint.proceed();
        else
            return new ResponseEntity<>("Invalid permissions", HttpStatus.FORBIDDEN);
    }

    @Around("execution(* com.taskboard.controller.TaskController.createTask(..)) && args(currentUser, createTaskRequest)")
    public ResponseEntity<?> validateUserPermsToCreateTask(
            ProceedingJoinPoint joinPoint,
            UserPrincipal currentUser,
            CreateTaskRequest createTaskRequest
    ) throws Throwable {
        if(userBoardPermissionsValidator
                .validate(currentUser.getId(), createTaskRequest.getBoardId(), LocalRoleName.LOCAL_ROLE_USER))
            return (ResponseEntity<?>) joinPoint.proceed();
        else
            return new ResponseEntity<>("Invalid permissions", HttpStatus.FORBIDDEN);
    }


}
