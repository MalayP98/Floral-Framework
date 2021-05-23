package com.dummyframework.exception;

public class AppContextException extends Exception {

  public AppContextException() {
    System.out.println("No defination found for WebApplicationContext.");
  }

}
