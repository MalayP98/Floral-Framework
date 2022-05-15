package com.dummyframework.utils;

import com.dummyframework.annotations.Config;
import com.dummyframework.annotations.Controller;
import com.dummyframework.annotations.Service;

public class FrameworkUtils {

  /**
   * Represents all the annotations which if put on a class makes it beanable, i.e. framework will
   * create a bean for that class and store it.
   */
  private final static Class<?>[] BEANABLE_ANNOTATIONS =
      {Config.class, Service.class, Controller.class};

  /**
   * Checks if provided class has any one of the beanable annotations.
   * 
   * @return returns first benable annotation if present, else returns null.
   */
  @SuppressWarnings("rawtypes")
  public static Class<?> getComponentClass(Class<?> clazz) {
    for (Class ann : BEANABLE_ANNOTATIONS) {
      if (clazz.isAnnotationPresent(ann)) {
        return ann;
      }
    }
    return null;
  }

  public static boolean isConfigurationBean(ComponentType type) {
    return (type == ComponentType.CONFIGURATION);
  }

  public static boolean isControllerBean(ComponentType type) {
    return (type == ComponentType.CONTROLLER);
  }

  public static boolean isServiceBean(ComponentType type) {
    return (type == ComponentType.SERVICE);
  }

  public static String className(Class<?> clazz) {
    return clazz.getPackageName() + "." + clazz.getName();
  }

  public static String beanName(Class<?> clazz) {
    Class<?> annotation = getComponentClass(clazz);
    String beanName = "";
    if (annotation == Config.class)
      beanName = clazz.getAnnotation(Config.class).name();
    if (annotation == Service.class)
      beanName = clazz.getAnnotation(Service.class).name();
    if (annotation == Controller.class)
      beanName = clazz.getAnnotation(Controller.class).name();
    if (beanName.equals(""))
      return clazz.getSimpleName();
    return beanName;
  }
}
