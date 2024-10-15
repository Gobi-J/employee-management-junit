package com.i2i.ems.helper;

import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * <p>
 *   Exception class that handles exceptions related to employee.
 * </p>
 */
@ResponseStatus
public class EmployeeException extends RuntimeException {
  public EmployeeException(String message, Throwable cause) {
    super(message, cause);
  }
}