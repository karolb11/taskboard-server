package com.taskboard.service;

import com.taskboard.model.Subscription;
import com.taskboard.model.Task;
import com.taskboard.model.User;
import com.taskboard.repository.SubscriptionRepository;
import com.taskboard.repository.TaskRepository;
import com.taskboard.repository.UserRepository;
import javassist.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    public SubscriptionService(SubscriptionRepository subscriptionRepository,
                               UserRepository userRepository,
                               TaskRepository taskRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
    }

    public void subscribeTask(Long subscriberId, Long taskId) throws NotFoundException {
        User subscriber = userRepository.findById(subscriberId)
                .orElseThrow(() -> new NotFoundException("user not found"));
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new NotFoundException("task not found"));

        Subscription subscription = Subscription.builder()
                .subscriber(subscriber)
                .task(task)
                .build();
        subscriptionRepository.save(subscription);
    }

    public boolean isTaskSubscribedByUser(Long subscriberId, Long taskId) {
        Optional<Subscription> subscription = subscriptionRepository.
                findAllBySubscriberIdAndTaskId(subscriberId, taskId);
        return subscription.isPresent();
    }

    public void deleteSubscription(Long subscriberId, Long taskId) throws NotFoundException {
        Subscription subscription = subscriptionRepository.findAllBySubscriberIdAndTaskId(subscriberId, taskId)
                .orElseThrow(() -> new NotFoundException("subscription not found"));
        subscriptionRepository.delete(subscription);
    }

    public List<Subscription> findSubscriptionByUserId(Long userId) {
        return subscriptionRepository.findAllBySubscriberIdOrderByTaskUpdatedAtDesc(userId);
    }


}
