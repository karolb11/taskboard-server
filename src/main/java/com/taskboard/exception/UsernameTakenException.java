package com.taskboard.exception;

public class UsernameTakenException extends Exception{
    public UsernameTakenException() {
        super("Username already taken");
    }
}
