package com.taskboard.repository;

import com.taskboard.model.LocalRole;
import com.taskboard.model.LocalRoleName;
import com.taskboard.model.Role;
import com.taskboard.model.RoleName;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LocalRoleRepository extends CrudRepository<LocalRole, Long> {
    Optional<LocalRole> findByName(LocalRoleName localRoleName);
}
