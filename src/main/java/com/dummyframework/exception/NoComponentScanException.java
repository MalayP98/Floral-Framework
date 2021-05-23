package com.dummyframework.exception;

public class NoComponentScanException extends Exception {

  public NoComponentScanException() {
    System.out.println("Component Scan not defined.");
  }

}
