package com.dummyframework.core;

import java.io.IOException;
import java.util.List;


public class DummyFramework {

  private static String ROOT_PACKAGE = null;

  private static WebApplicationContext webApplicationContext;

  public static void setRootPackage(String rootPackage) {
    ROOT_PACKAGE = rootPackage;
  }

  public static void run(Class clazz) throws IOException {
    String rootPackage = clazz.getPackageName();
    setRootPackage(rootPackage);
    List<String> classes = ComponentScan.startComponentScan(ROOT_PACKAGE);
    for (String scanedClass : classes) {
      System.out.println(scanedClass);
    }
  }

  private static void initContext(List<String> classes) {
    webApplicationContext = new WebApplicationContext(classes);
  }
}
