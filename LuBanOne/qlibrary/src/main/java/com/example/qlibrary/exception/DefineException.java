package com.example.qlibrary.exception;

/**
 * Created by Administrator on 2017/5/27.
 */
public class DefineException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public DefineException() {
    super();
  }

  public DefineException(String message, Throwable cause) {
    super(message, cause);
  }

  public DefineException(String message) {
    super(message);
  }

  public DefineException(Throwable cause) {
    super(cause);
  }

}
