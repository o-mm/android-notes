package com.example.ov_mm.notes.service.network;

public class SyncException extends Exception {

    public SyncException(String message) {
        super(message);
    }

    public SyncException(String message, Throwable cause) {
        super(message, cause);
    }
}
