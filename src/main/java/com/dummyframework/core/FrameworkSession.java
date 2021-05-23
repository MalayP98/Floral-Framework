package com.dummyframework.core;

import javax.servlet.http.HttpSession;

public class FrameworkSession {

  private static HttpSession frameworkSession = null;

  public static void setSession(HttpSession session) {
    frameworkSession = session;
  }

  public static void add(String key, Object value) {
    frameworkSession.setAttribute(key, value);
  }

  public static Object get(String key) {
    return frameworkSession.getAttribute(key);
  }

  public static void invalidate() {
    frameworkSession.invalidate();
  }

  public static boolean isNull() {
    return (frameworkSession == null);
  }
}
