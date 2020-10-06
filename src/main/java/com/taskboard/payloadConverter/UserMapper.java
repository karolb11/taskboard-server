package com.taskboard.payloadConverter;

import com.taskboard.model.User;
import com.taskboard.payload.UserResponse;

public class UserMapper {
    public static UserResponse userToUserResponse(User user) {
        return new UserResponse(user.getId(), user.getUsername(), user.getName(), user.getEmail());
    }
}
