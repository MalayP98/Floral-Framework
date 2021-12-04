package com.dummyframework.core;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class ScanComponent {

  private static String modifyPackage(String packageName, File directory) {
    String[] path = directory.toString().split("/");
    StringBuilder newPackage = new StringBuilder(packageName);
    newPackage.append("." + path[path.length - 1]);
    return newPackage.toString();
  }

  private static List<String> findClasses(File directory, String packageName, boolean isDirectory) {
    if (isDirectory)
      packageName = modifyPackage(packageName, directory);
    List<String> classes = new ArrayList<String>();
    if (directory.exists()) {
      File[] files = directory.listFiles();
      for (File file : files) {
        if (file.isDirectory())
          classes.addAll(findClasses(file, packageName, true));
        else if (file.getName().endsWith(".class")) {
          String className =
              packageName + '.' + file.getName().substring(0, file.getName().length() - 6);
          classes.add(className);
        }
      }
    }
    return classes;
  }

  public static Set<String> startComponentScan(String rootPackage) throws IOException {
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    String path = rootPackage.replace('.', '/');
    Enumeration<URL> resources = classLoader.getResources(path);
    List<File> directories = new ArrayList<File>();
    while (resources.hasMoreElements()) {
      URL resource = resources.nextElement();
      directories.add(new File(resource.getFile()));
    }
    Set<String> classes = new TreeSet<String>();
    for (File directory : directories) {
      classes.addAll(findClasses(directory, rootPackage, false));
    }
    return classes;
  }
}
