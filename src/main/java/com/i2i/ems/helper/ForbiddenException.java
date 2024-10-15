package com.i2i.ems.helper;

/**
 * <p>
 * ForbiddenException class is a custom exception class for handling forbidden exceptions.
 * </p>
 *
 * @version 1.0
 */
public class ForbiddenException extends RuntimeException {

    public ForbiddenException(String message) {
        super(message);
    }
}
