package com.taskboard.repository;

import com.taskboard.model.TaskPriority;
import com.taskboard.model.TaskPriorityName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskPriorityRepository extends JpaRepository<TaskPriority, Long> {
    List<TaskPriority> findAllByOrderById();
    Optional<TaskPriority> findByName(TaskPriorityName priorityName);
}
