package com.dummyframework.exception;

public class NoPatternMatchedException extends Exception {

  public NoPatternMatchedException() {
    super("Pattern did not matched");
  }

}
