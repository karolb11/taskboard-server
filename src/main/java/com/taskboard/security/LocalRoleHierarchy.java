package com.taskboard.security;

import com.taskboard.model.LocalRole;
import com.taskboard.model.LocalRoleName;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class LocalRoleHierarchy {
    private static final Map<LocalRoleName, Integer> roleHierarchy = new HashMap<LocalRoleName, Integer>() {{
        put(LocalRoleName.LOCAL_ROLE_VIEWER, 1);
        put(LocalRoleName.LOCAL_ROLE_USER, 2);
        put(LocalRoleName.LOCAL_ROLE_OWNER, 3);
    }};

    public Map<LocalRoleName, Integer> getRoleHierarchy() {
        return roleHierarchy;
    }

    public boolean isContaining(LocalRoleName outerRole, LocalRoleName innerRole) {
        if(roleHierarchy.get(outerRole) >= roleHierarchy.get(innerRole)) return true;
        else return false;
    }

}
