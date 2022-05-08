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
      {Config.class, Controller.class, Service.class};

  /**
   * Checks if provided class has any one of the beanable annotations.
   * 
   * @return returns first benable annotation if present, else returns null.
   */
  @SuppressWarnings("rawtypes")
  public static Class<?> getBeanableAnnotation(Class<?> clazz) {
    for (Class ann : BEANABLE_ANNOTATIONS) {
      if (clazz.isAnnotationPresent(ann)) {
        return ann;
      }
    }
    return null;
  }

  public static boolean isConfigurationBean(BeanType type) {
    return (type == BeanType.CONFIGURATION);
  }

  public static boolean isControllerBean(BeanType type) {
    return (type == BeanType.CONTROLLER);
  }

  public static boolean isServiceBean(BeanType type) {
    return (type == BeanType.SERVICE);
  }
}
