package com.taskboard.payloadConverter;

import com.taskboard.model.Role;
import com.taskboard.model.User;
import com.taskboard.payload.UserResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class UserMapper {
    public static UserResponse userToUserResponse(User user) {
        //find highest role
        List<Role> roles = new ArrayList<>(user.getRoles());
        roles.sort((d1, d2) -> (int) (d1.getId() - d2.getId()));
        Role role = roles.get(roles.size()-1);

        return new UserResponse(user.getId(), role, user.getUsername(), user.getName(), user.getEmail());
    }
}
