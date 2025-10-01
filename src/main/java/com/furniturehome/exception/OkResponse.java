package com.furniturehome.exception;

public class OkResponse extends RuntimeException {
    public OkResponse(String message) {
        super(message);
    }
}
