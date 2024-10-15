package com.i2i.ems.helper;

/**
 * <p>
 *   Exception class that handles unauthorized exceptions.
 * </p>
 */
public class UnAuthorizedException extends RuntimeException {
    public UnAuthorizedException(String message) {
        super(message);
    }
}