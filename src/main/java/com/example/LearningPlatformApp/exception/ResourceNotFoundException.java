package com.example.LearningPlatformApp.exception;

public class ResourceNotFoundException
        extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
// لما نبحث عن Course أو Category أو User ومش نلاقيه.
//404 Not Found