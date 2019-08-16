package com.actualplayer.modpermissions.model;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class MessageTimestamp {

    private String message;
    private LocalTime timestamp;

    public MessageTimestamp(String message, LocalTime timestamp) {
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalTime timestamp) {
        this.timestamp = timestamp;
    }
}
