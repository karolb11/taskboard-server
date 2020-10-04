package com.taskboard.controller;

import com.taskboard.security.CurrentUser;
import com.taskboard.security.UserPrincipal;
import com.taskboard.service.SubscriptionService;
import javassist.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;


@RestController
@PreAuthorize("hasRole('USER')")
@RequestMapping("/api/subscription")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @PostMapping
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
    public ResponseEntity<?> isTaskSubscribedByUser(@CurrentUser UserPrincipal currentUser,
                                                    @PathParam("taskId") Long taskId) {
        boolean res = subscriptionService.isTaskSubscribedByUser(currentUser.getId(), taskId);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @DeleteMapping
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
