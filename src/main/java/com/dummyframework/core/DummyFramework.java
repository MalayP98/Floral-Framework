package com.dummyframework.core;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;
import com.dummyframework.annotations.ComponentScan;
import com.dummyframework.exception.AppContextException;
import com.dummyframework.exception.NoComponentScanException;
import com.dummyframework.logger.Logger;

public class DummyFramework {

  private static String ROOT_PACKAGE = null;
  private static Logger logger = new Logger(DummyFramework.class);
  private static ApplicationContext applicationContext = null;

  public static void setRootPackage(String rootPackage) {
    ROOT_PACKAGE = rootPackage;
  }

  public static String getRootPackage() {
    return ROOT_PACKAGE;
  }

  public static <T> void run(Class<T> clazz)
      throws IOException, NoComponentScanException, ClassNotFoundException,
      IllegalArgumentException, IllegalAccessException, NoSuchMethodException, SecurityException,
      InstantiationException, InvocationTargetException, AppContextException {
    printBanner();
    if (!isComponentScanPossible(clazz))
      throw new NoComponentScanException();
    String rootPackage = getScanningPackage(clazz);
    setRootPackage(rootPackage);
    logger.info("Starting Component Scan.");
    Set<String> classes = ScanComponent.startComponentScan(ROOT_PACKAGE);
    logger.info("Component scan completed successfully.");
    initContext(classes);
  }

  public static String getScanningPackage(Class<?> clazz) {
    ComponentScan annotation = (ComponentScan) clazz.getAnnotation(ComponentScan.class);
    String packageName = annotation.packageName();
    if (packageName.equals(""))
      return clazz.getPackageName();
    return packageName;
  }

  private static boolean isComponentScanPossible(Class<?> clazz) {
    Annotation annotation = clazz.getAnnotation(ComponentScan.class);
    if (annotation == null)
      return false;
    return true;
  }

  private static void initContext(Set<String> classes)
      throws ClassNotFoundException, AppContextException {
    System.out.println("\n\n ****** DummyFramework.initContext() ***** \n\n");
    applicationContext = new ApplicationContext(classes);
  }

  public static ApplicationContext getWebApplicationContext() throws AppContextException {
    if (applicationContext != null)
      return applicationContext;
    throw new AppContextException();
  }

  private static void printBanner() {
    try {
      // TODO : set location property.
      BufferedReader br = new BufferedReader(new FileReader(
          "/Users/malaypandey/Work/Projects/Dummy-Framework/src/main/java/com/dummyframework/utils/banner.txt"));
      String line;
      while ((line = br.readLine()) != null) {
        System.out.println(line);
      }
    } catch (IOException e1) {
      logger.error("No file found by name \"banner.txt\"");
    }
  }
}
