package com.dummyframework.core;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import com.dummyframework.annotations.ComponentScan;
import com.dummyframework.core.bean.BeanDefinition;
import com.dummyframework.core.bean.BeanDefinitionRegistry;
import com.dummyframework.core.bean.Reader;
import com.dummyframework.exception.AppContextException;
import com.dummyframework.exception.NoComponentScanException;
import com.dummyframework.logger.Logger;

public class DummyFramework {

  private static String ROOT_PACKAGE = null;
  private static Logger LOG = new Logger(DummyFramework.class);
  private static ApplicationContext applicationContext = null;
  private static Reader reader = new Reader();

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
    Properties.set(DefaultProperties.MAIN_CLASS_NAME, clazz.getName());
    printBanner();
    List<Class<?>> scannedClasses = new ArrayList<>(scanClasses(clazz));
    reader.register(scannedClasses);
    loadContext(scannedClasses);
  }

  private static <T> Set<Class<?>> scanClasses(Class<T> clazz)
      throws NoComponentScanException, IOException, ClassNotFoundException {
    if (!isComponentScanPossible(clazz))
      throw new NoComponentScanException();
    String rootPackage = getScanningPackage(clazz);
    setRootPackage(rootPackage);
    LOG.info("Starting Component Scan.");
    Set<Class<?>> classes = ScanComponent.startComponentScan(ROOT_PACKAGE);
    LOG.info("Component scan completed successfully.");
    return classes;
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

  private static void loadContext(List<Class<?>> classes)
      throws ClassNotFoundException, AppContextException {
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
      LOG.error("No file found by name \"banner.txt\"");
    }
  }
}
