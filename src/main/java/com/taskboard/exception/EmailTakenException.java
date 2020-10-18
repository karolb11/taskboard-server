package com.taskboard.exception;

public class EmailTakenException extends Exception {
    public EmailTakenException() {
        super("Email already taken");
    }
}
