package com.dummyframework.core;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import com.dummyframework.annotations.ComponentScan;
import com.dummyframework.exception.AppContextException;
import com.dummyframework.exception.NoComponentScanException;

public class DummyFramework {

  private static String ROOT_PACKAGE = null;

  private static ApplicationContext applicationContext = null;

  public static void setRootPackage(String rootPackage) {
    ROOT_PACKAGE = rootPackage;
  }

  public static String getRootPackage() {
    return ROOT_PACKAGE;
  }

  public static <T> void run(Class<T> clazz) throws IOException, NoComponentScanException, ClassNotFoundException,
      IllegalArgumentException, IllegalAccessException, NoSuchMethodException, SecurityException,
      InstantiationException, InvocationTargetException, AppContextException {
    if (!isComponentScanPossible(clazz))
      throw new NoComponentScanException();
    String rootPackage = getScanningPackage(clazz);
    setRootPackage(rootPackage);
    List<String> classes = ScanComponent.startComponentScan(ROOT_PACKAGE);
    initContext(classes);
  }

  public static String getScanningPackage(Class clazz) {
    ComponentScan annotation = (ComponentScan) clazz.getAnnotation(ComponentScan.class);
    String packageName = annotation.packageName();
    if (packageName.equals(""))
      return clazz.getPackageName();
    return packageName;
  }

  private static boolean isComponentScanPossible(Class clazz) {
    @SuppressWarnings("unchecked")
    Annotation annotation = clazz.getAnnotation(ComponentScan.class);
    if (annotation == null)
      return false;
    return true;
  }

  private static void initContext(List<String> classes)
      throws ClassNotFoundException, AppContextException, IllegalArgumentException, IllegalAccessException,
      NoSuchMethodException, SecurityException, InstantiationException, InvocationTargetException {
    applicationContext = new ApplicationContext(classes);
  }

  public static ApplicationContext getWebApplicationContext() throws AppContextException {
    if (applicationContext != null)
      return applicationContext;
    throw new AppContextException();
  }
}
