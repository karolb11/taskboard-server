package com.taskboard.repository;

import com.taskboard.model.TaskState;
import com.taskboard.model.TaskStateName;
import com.taskboard.model.TaskType;
import com.taskboard.model.TaskTypeName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaskTypeRepository extends JpaRepository<TaskType, Long> {
    Optional<TaskType> findByName(TaskTypeName typeName);
}
