package com.taskboard.payload;

import lombok.Data;

@Data
public class UpdateUserRequest {
    private UpdateType type;
    private String name;
    private String email;
    private String newPassword;
    private String confirmNewPassword;
    private String currentPassword;

    public enum UpdateType {
        UPDATE_DATA,
        CHANGE_PASSWORD,
        DEACTIVATE_ACCOUNT
    }
}
