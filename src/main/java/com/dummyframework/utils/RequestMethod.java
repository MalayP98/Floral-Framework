package com.dummyframework.utils;

public enum RequestMethod {

  GET, POST, PUT, PATCH, DELETE;

  public static RequestMethod toRequestMethod(String method) throws Exception {
    switch (method.toLowerCase()) {
      case "get":
        return GET;
      case "post":
        return POST;
      case "put":
        return PUT;
      case "patch":
        return PATCH;
      case "delete":
        return DELETE;
      default:
        throw new Exception("Not a valid request method.");
    }
  }

}
