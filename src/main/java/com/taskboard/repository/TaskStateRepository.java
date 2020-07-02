package com.taskboard.repository;

import com.taskboard.model.TaskState;
import com.taskboard.model.TaskStateName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskStateRepository extends JpaRepository<TaskState, Long>{
    List<TaskState> findAllByOrderById();
    Optional<TaskState> findByName(TaskStateName stateName);
}
