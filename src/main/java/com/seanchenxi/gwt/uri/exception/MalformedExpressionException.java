package com.seanchenxi.gwt.uri.exception;

/**
 * @author Xi CHEN
 * @since 13/12/15.
 */
public class MalformedExpressionException extends RuntimeException {

  private int position;

  public MalformedExpressionException(String message, int position, Throwable throwable) {
    super(message, throwable);
    this.position = position;
  }

  public MalformedExpressionException(String message, Throwable throwable) {
    super(message, throwable);
  }

  public MalformedExpressionException(String message) {
    super(message);
  }

  public void setPosition(int position) {
    this.position = position;
  }

  public int getPosition() {
    return position;
  }
}
