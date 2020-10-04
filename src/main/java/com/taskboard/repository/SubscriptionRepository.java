package com.taskboard.repository;

import com.taskboard.model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    Optional<Subscription> findAllBySubscriberIdAndTaskId(Long subscriberId, Long taskId);
    List<Subscription> findAllBySubscriberIdOrderByTaskUpdatedAtDesc(Long subscriberId);
}
