package com.dummyframework.core;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import com.dummyframework.annotations.Autowired;
import com.dummyframework.annotations.Controller;
import com.dummyframework.annotations.RequestMapping;
import com.dummyframework.utils.FrameworkUtils;

public class WebApplicationContext {

  private FrameworkUtils frameworkUtils = new FrameworkUtils();

  private HashMap<String, Object> beanMap = new HashMap<String, Object>();
  private HashMap<String, List<Object>> urlMap = new HashMap<String, List<Object>>();

  public WebApplicationContext(List<String> classes)
      throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException,
      NoSuchMethodException, SecurityException, InstantiationException, InvocationTargetException {
    instantiateBeans(classes);
  }

  private void instantiateBeans(List<String> classes) throws ClassNotFoundException,
      NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException,
      IllegalArgumentException, InvocationTargetException {
    for (String className : classes) {
      if(frameworkUtils.isBeanable(className)){
        Object bean = frameworkUtils.createObject(className);
        beanMap.put(className, bean);
        mapUrls(className);
      }
    }
    autowireFields(classes);
  }

  private void mapUrls(String className) throws ClassNotFoundException {
    Class clazz = Class.forName(className);
    if(Objects.nonNull(clazz.getAnnotation(Controller.class))){
      Method[] methods = clazz.getDeclaredMethods();
      for (Method method : methods) {
        RequestMapping annotation = method.getAnnotation(RequestMapping.class);
        boolean isRequestMapped = (annotation != null);
        if (isRequestMapped) {
          List<Object> specification = new ArrayList<Object>();
          Parameter[] params = method.getParameters();
          String url = annotation.value();
          String type = annotation.method().toString();
          String key = url + "#" + type;
          String value = clazz.getName() + "#" + method.getName();
          specification.add(value);
          specification.add(params);
          urlMap.put(key, specification);
        }
      }
    }
  }

  private void autowireFields(List<String> classes)
      throws InstantiationException, IllegalAccessException, IllegalArgumentException,
      InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException {
        for(String className : classes){
          Object bean = getBean(className);
          Class clazz = Class.forName(className);
          Field[] fields = clazz.getDeclaredFields();
          for(Field field : fields){
            field.setAccessible(true);
            Autowired autowired = field.getAnnotation(Autowired.class);
            if(Objects.nonNull(autowired) && frameworkUtils.isBeanable(className)){
              field.set(bean, getBean(field.getType().getName()));
            }
          }
        }
  }

  public HashMap<String, List<Object>> getUrlMap() {
    return urlMap;
  }

  public HashMap<String, Object> getBeanMap() {
    return beanMap;
  }

  public Object getBean(String className) {
    return beanMap.get(className);
  }
}
