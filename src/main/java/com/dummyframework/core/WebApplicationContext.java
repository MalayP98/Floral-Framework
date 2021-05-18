package com.dummyframework.core;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import com.dummyframework.annotations.Autowired;
import com.dummyframework.annotations.Controller;
import com.dummyframework.annotations.RequestMapping;

public class WebApplicationContext {

  public HashMap<String, Object> beanMap = new HashMap<String, Object>();
  public HashMap<String, String> urlMap = new HashMap<String, String>();

  public WebApplicationContext(List<String> classes)
      throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException,
      NoSuchMethodException, SecurityException, InstantiationException, InvocationTargetException {
    instantiateBeans(classes);
  }

  private void instantiateBeans(List<String> classes) throws ClassNotFoundException,
      NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException,
      IllegalArgumentException, InvocationTargetException {
    for (String className : classes) {
      Object bean = null;
      Class clazz = Class.forName(className);
      Controller annotation = (Controller) clazz.getAnnotation(Controller.class);
      boolean isController = (annotation != null);
      if (isController) {
        Constructor classConstructor = clazz.getConstructor(null);
        bean = classConstructor.newInstance(null);
        autowireFields(clazz, bean);
        mapUrls(clazz);
        beanMap.put(className, bean);
      }
    }
  }

  private void mapUrls(Class clazz) {
    Method[] methods = clazz.getDeclaredMethods();
    for (Method method : methods) {
      RequestMapping annotation = method.getAnnotation(RequestMapping.class);
      boolean isRequestMapped = (annotation != null);
      if (isRequestMapped) {
        String url = annotation.url();
        String type = annotation.type().toString();
        String key = url + "#" + type;
        String value = clazz.toString() + "#" + method.toString();
        urlMap.put(key, value);
      }
    }
  }

  private void autowireFields(Class clazz, Object bean)
      throws InstantiationException, IllegalAccessException, IllegalArgumentException,
      InvocationTargetException, NoSuchMethodException, SecurityException {
    Field[] fields = clazz.getDeclaredFields();
    for (Field field : fields) {
      Autowired autowired = field.getAnnotation(Autowired.class);
      boolean isAutowired = (autowired != null);
      if (isAutowired) {
        field.setAccessible(true);
        Class fieldClass = field.getType();
        Constructor classConstructor = fieldClass.getConstructor(null);
        Object fieldBean = classConstructor.newInstance(null);
        field.set(bean, fieldBean);
      }
    }
  }

}
