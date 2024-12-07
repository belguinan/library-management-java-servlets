package com.library.project.exceptions;

public class InvalidArgumentException extends Exception {
    /**
     * @param message
     */
    public InvalidArgumentException(String message) {
        super(message);
    }

    public InvalidArgumentException() {
        super();
    }
}
