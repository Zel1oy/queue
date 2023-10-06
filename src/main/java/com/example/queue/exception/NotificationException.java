package com.example.queue.exception;

public class NotificationException extends RuntimeException {
    public NotificationException(String message) {
        super(message);
    }

    public NotificationException(Throwable e) {
        super(e);
    }

    public NotificationException(String message, Throwable e) {
        super(message, e);
    }
}
