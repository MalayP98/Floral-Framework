package com.dummyframework.core;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import com.dummyframework.utils.FrameworkUtils;

public class ScanComponent {

  private static String modifyPackage(String packageName, File directory) {
    String[] path = directory.toString().split("/");
    StringBuilder newPackage = new StringBuilder(packageName);
    newPackage.append("." + path[path.length - 1]);
    return newPackage.toString();
  }

  private static List<Class<?>> findClasses(File directory, String packageName, boolean isDirectory)
      throws ClassNotFoundException {
    if (isDirectory)
      packageName = modifyPackage(packageName, directory);
    List<Class<?>> classes = new ArrayList<Class<?>>();
    if (directory.exists()) {
      File[] files = directory.listFiles();
      for (File file : files) {
        if (file.isDirectory())
          classes.addAll(findClasses(file, packageName, true));
        else if (file.getName().endsWith(".class")) {
          String className =
              packageName + '.' + file.getName().substring(0, file.getName().length() - 6);
          classes.add(FrameworkUtils.toClass(className));
        }
      }
    }
    return classes;
  }

  public static Set<Class<?>> startComponentScan(String rootPackage)
      throws IOException, ClassNotFoundException {
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    String path = rootPackage.replace('.', '/');
    Enumeration<URL> resources = classLoader.getResources(path);
    List<File> directories = new ArrayList<File>();
    while (resources.hasMoreElements()) {
      URL resource = resources.nextElement();
      directories.add(new File(resource.getFile()));
    }
    Set<Class<?>> classes = new TreeSet<Class<?>>(new ClassNameComparator());
    for (File directory : directories) {
      classes.addAll(findClasses(directory, rootPackage, false));
    }
    return classes;
  }

  private static class ClassNameComparator implements Comparator<Class<?>> {

    private String removePackage(Class<?> clazz) {
      String[] segregatedName = clazz.getName().split("\\.");
      return segregatedName[segregatedName.length - 1];
    }

    @Override
    public int compare(Class<?> classNameA, Class<?> classNameB) {
      return removePackage(classNameA).compareTo(removePackage(classNameB));
    }
  }
}
