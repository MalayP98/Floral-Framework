package com.dummyframework.core.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Add path in form of a graph. For example if there are 2 paths
 * 
 * P1 : /example/{var1}/path
 * 
 * P2 : /example/id/path/{var2}
 * 
 * Then the map will looking like this,
 * 
 * path -> [*]
 * 
 * * -> [path]
 * 
 * example -> [*, id]
 * 
 * id -> [path]
 * 
 * Always saves generic path. All dynamic sub paths will be converted to '*'. With reference to P1,
 * map will contain /example/'*'/path.
 */
public class Path {

  private static final String ROOT = "#";

  // here '#' represents root node.
  private static Map<String, List<String>> pathMap = new HashMap<>();
  static {
    pathMap.put(ROOT, new ArrayList<>());
  }

  /**
   * Splits the path by '/' into sub paths. /exapmle/path/id will become [example, path, id]. This
   * is array is then mapped with ROOT.
   */
  public String addPath(String path) {
    String[] subPath = path.split("/");
    int startingIndex = 1;
    return addPath(subPath, startingIndex, pathMap.get(ROOT));
  }

  /**
   * Added current index of sub path to the children of current root if it is not present already.
   * If the sub path is dynamic then instead of sub path '*' is stored as the child for current
   * root. If the child i.e current index of sub path has no key in the map then a new arrat is
   * assigned to that child. The current child then becomes the new root for the next iteration.
   */
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

  /**
   * checks if the node has a solid value or is it variable. Varible is detected if the node starts
   * with '{' and ends with '}'. Return true if the node is a varibale.
   */
  public static boolean isDynamic(String pathNode) {
    return (pathNode.startsWith("{") && pathNode.endsWith("}"));
  }

  /**
   * Take a path like, /example/{var1}/{var2}/path1/path2 and check if some generic path of this
   * sort exist in the map.
   * 
   * @return : returns the matched path.
   */
  public String matchPath(String path) throws Exception {
    String[] subPath = path.split("/");
    int startIndex = 1;
    return matchPath(subPath, startIndex, pathMap.get(ROOT));
  }

  /**
   * Funtions similar to the addPath(String[], int, List<String>). The only diffrence is instead of
   * add current index of sub path i.e child, the is child searched in the children of the current
   * root. If the child is present then it becomes the new root for next iteration.
   * 
   * A tricky situation here might be like,
   * 
   * P1 : /example/{var}/random1
   * 
   * P2 : /example/path/random2
   * 
   * P3(path to matched) : /example/path/random1
   * 
   * P3 can be matched with P1. But there is no 'random1' child of 'path'. So if a path doesn't
   * match we try to see if the one of the child of current root is '*'. If this is the case we also
   * match '*' and then continue to find the path.
   */
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
}
