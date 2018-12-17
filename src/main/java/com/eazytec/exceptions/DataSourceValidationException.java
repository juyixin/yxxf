package com.eazytec.exceptions;


/**
 * Runtime exception that is the superclass of all sql validation
 * related things that we will check manually.
 * 
 * @author madan
 */
public class DataSourceValidationException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public DataSourceValidationException(String message, Throwable cause) {
    super(message, cause);
  }

  public DataSourceValidationException(String message) {
    super(message);
  }
}
