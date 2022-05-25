package com.dummyframework.core.handler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Path {

  private static final String ROOT = "#";

  // here '#' represents root node.
  private static Map<String, List<String>> pathMap = new HashMap<>();
  static {
    pathMap.put(ROOT, new ArrayList<>());
  }

  public String addPath(String path) {
    String[] subPath = path.split("/");
    int startingIndex = 1;
    return addPath(subPath, startingIndex, pathMap.get(ROOT));
  }

  private String addPath(String[] subPath, int currentIndex, List<String> children) {
    if (currentIndex < subPath.length) {
      String child = isDynamic(subPath[currentIndex]) ? "*" : subPath[currentIndex];
      if (Objects.nonNull(children)) {
        if (!children.contains(child))
          children.add(child);
      }
      if (!pathMap.containsKey(child)) {
        pathMap.put(child, new ArrayList<String>());
      }
      return "/" + child + addPath(subPath, currentIndex + 1, pathMap.get(child));
    }
    return "";
  }

  public static boolean isDynamic(String pathNode) {
    return (pathNode.startsWith("{") && pathNode.endsWith("}"));
  }

  public String matchPath(String path) throws Exception {
    String[] subPath = path.split("/");
    int startIndex = 1;
    return matchPath(subPath, startIndex, pathMap.get(ROOT));
  }

  public String matchPath(String[] subPath, int currentIndex, List<String> children)
      throws Exception {
    if (currentIndex < subPath.length) {
      String next = "", child = "";
      if (children.contains(subPath[currentIndex])) {
        child = subPath[currentIndex];
        next = matchPath(subPath, currentIndex + 1, pathMap.get(subPath[currentIndex]));
      }
      if (next.isEmpty() && children.contains("*")) {
        child = "*";
        next = matchPath(subPath, currentIndex + 1, pathMap.get("*"));
      }
      if (!next.isEmpty()) {
        return "/" + child + next;
      }
    } else
      return "/";
    return "";
  }

  // remove
  public void displayPaths() {
    for (Map.Entry<String, List<String>> x : pathMap.entrySet()) {
      System.out.println(x.getKey() + " --> " + Arrays.toString(x.getValue().toArray()));
    }
  }
}
