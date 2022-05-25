package com.dummyframework.core.handler;

import java.util.HashMap;
import java.util.Map;
import com.dummyframework.utils.RequestMethod;

public class HandlerRegistry {

  private static HandlerRegistry INSTANCE = null;

  private static Path matcher = new Path();

  private HandlerRegistry() {
  }

  public static HandlerRegistry getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new HandlerRegistry();
    }
    return INSTANCE;
  }

  private static Map<String, HandlerDetail> handleRegistry = new HashMap<>();

  public void addHandler(String uri, HandlerDetail detail) {
    handleRegistry.put(uri, detail);
  }

  public HandlerDetail getHandlerDetail(String uri, RequestMethod method) throws Exception {
    String matchedPath = matcher.matchPath(uri);
    if (matchedPath.endsWith("/"))
      matchedPath = matchedPath.substring(0, matchedPath.length() - 1);
    if (handleRegistry.containsKey(matchedPath + "#" + method.toString())) {
      return handleRegistry.get(matchedPath + "#" + method.toString());
    }
    throw new Exception("No path '" + uri + "' exists.");
  }

  public void display() {
    for (Map.Entry<String, HandlerDetail> x : handleRegistry.entrySet()) {
      System.out
          .println(x.getKey() + " -> " + x.getValue().getHandler() + "#" + x.getValue().getUri());
    }
  }

  // public void add(String url, String method, HandlerDetails details) {
  // urlRegistry.put(generateKey(url, method), details);
  // }

  // public HandlerDetails get(String url, String method) {
  // String key = generateKey(url, method);
  // // throw error here.
  // if (!urlRegistry.containsKey(key)) {
  // return null;
  // }
  // return urlRegistry.get(key);
  // }

  // public HandlerDetails get(Request request) {
  // return get(request.getUrl(), request.getMethod());
  // }

  // private String generateKey(String url, String method) {
  // return url + "#" + method;
  // }
}
