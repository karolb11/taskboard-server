package com.taskboard.controller;

import com.taskboard.security.CurrentUser;
import com.taskboard.security.UserPrincipal;
import com.taskboard.service.SubscriptionService;
import com.taskboard.utils.ResourceIdUtils;
import javassist.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;


@RestController
@PreAuthorize("hasRole('USER')")
@RequestMapping("/api/subscription")
@AllArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;
    private final ResourceIdUtils resourceIdUtils;

    @PostMapping
    @PreAuthorize("hasAuthority('board'+@resourceIdUtils.getBoardIdByTaskId(#taskId)+':'+'LOCAL_ROLE_VIEWER')" +
            "or hasAuthority('board'+@resourceIdUtils.getBoardIdByTaskId(#taskId)+':'+'LOCAL_ROLE_USER')" +
            "or hasAuthority('board'+@resourceIdUtils.getBoardIdByTaskId(#taskId)+':'+'LOCAL_ROLE_OWNER')")
    public ResponseEntity<?> subscribeTask(@CurrentUser UserPrincipal currentUser,
                                           @RequestBody Long taskId) {
        try {
            subscriptionService.subscribeTask(currentUser.getId(), taskId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    //not needs to be secured
    public ResponseEntity<?> isTaskSubscribedByUser(@CurrentUser UserPrincipal currentUser,
                                                    @PathParam("taskId") Long taskId) {
        boolean res = subscriptionService.isTaskSubscribedByUser(currentUser.getId(), taskId);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @DeleteMapping
    //not needs to be secured
    public ResponseEntity<?> deleteSubscription(@CurrentUser UserPrincipal currentUser,
                                                @PathParam("taskId") Long taskId) {
        try {
            subscriptionService.deleteSubscription(currentUser.getId(), taskId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
