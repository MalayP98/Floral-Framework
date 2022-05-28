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
}
