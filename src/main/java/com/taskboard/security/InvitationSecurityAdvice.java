package com.taskboard.security;

import com.taskboard.model.LocalRoleName;
import com.taskboard.payload.BoardLocalGroupUserLinkRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Aspect
@Configuration
public class InvitationSecurityAdvice {
    final
    UserBoardPermissionsValidator userBoardPermissionsValidator;
    final
    UserInvitationSecurityValidator userInvitationSecurityValidator;

    public InvitationSecurityAdvice(UserBoardPermissionsValidator userBoardPermissionsValidator, UserInvitationSecurityValidator userInvitationSecurityValidator) {
        this.userBoardPermissionsValidator = userBoardPermissionsValidator;
        this.userInvitationSecurityValidator = userInvitationSecurityValidator;
    }

    @Around("execution(* com.taskboard.controller.InvitationController.inviteUserToBoard(..)) && args(currentUser, link)")
    public ResponseEntity<?> validateUserPermsToAddMembers(
            ProceedingJoinPoint joinPoint,
            UserPrincipal currentUser,
            BoardLocalGroupUserLinkRequest link
    ) throws Throwable {
        if(userBoardPermissionsValidator
                .validate(currentUser.getId(), link.getBoardId(), LocalRoleName.LOCAL_ROLE_OWNER))
            return (ResponseEntity<?>) joinPoint.proceed();
        else
            return new ResponseEntity<>("Invalid permissions", HttpStatus.FORBIDDEN);
    }

    @Around("execution(* com.taskboard.controller.InvitationController.acceptInvitation(..)) && args(currentUser, invitationId)")
    public ResponseEntity<?> validateInvitationAcceptRequest(
            ProceedingJoinPoint joinPoint,
            UserPrincipal currentUser,
            Long invitationId
    ) throws Throwable {
        if(userInvitationSecurityValidator.validate(invitationId, currentUser.getId()))
            return (ResponseEntity<?>) joinPoint.proceed();
        else return new ResponseEntity<>("Invalid request", HttpStatus.FORBIDDEN);
    }
}
