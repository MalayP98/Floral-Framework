package com.dummyframework.core;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import com.dummyframework.annotations.Autowired;
import com.dummyframework.annotations.Controller;
import com.dummyframework.annotations.RequestMapping;
import com.dummyframework.annotations.ResponseBody;
import com.dummyframework.utils.FrameworkUtils;

public class WebApplicationContext {

  private FrameworkUtils frameworkUtils = new FrameworkUtils();

  private HashMap<String, Object> beanMap = new HashMap<String, Object>();
  private HashMap<String, HandlerDetails> urlMap = new HashMap<String, HandlerDetails>();

  public WebApplicationContext(List<String> classes)
      throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, NoSuchMethodException,
      SecurityException, InstantiationException, InvocationTargetException {
    instantiateBeans(classes);
  }

  private void instantiateBeans(List<String> classes)
      throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException,
      IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    for (String clazz : classes) {
      if (frameworkUtils.isBeanable(clazz)) {
        Object bean = frameworkUtils.createObject(clazz);
        beanMap.put(clazz, bean);
        mapUrls(clazz);
      }
    }
    autowireFields(classes);
  }

  private void mapUrls(String className) throws ClassNotFoundException {
    Class<?> clazz = Class.forName(className);
    boolean isResponseBody = (clazz.getAnnotation(ResponseBody.class) != null);
    if (Objects.nonNull(clazz.getAnnotation(Controller.class))) {
      Method[] methods = clazz.getDeclaredMethods();
      for (Method method : methods) {
        RequestMapping annotation = method.getAnnotation(RequestMapping.class);
        boolean isRequestMapped = (annotation != null);
        if (isRequestMapped) {
          HandlerDetailsBuilder details = new HandlerDetailsBuilder();
          details.setComponent(beanMap.get(className));
          details.setCalledMethod(method);
          if (isResponseBody) {
            details.setAtResponseBody(isResponseBody);
          } else {
            isResponseBody = (method.getAnnotation(ResponseBody.class) != null);
            details.setAtResponseBody(isResponseBody);
          }
          String endpoint = annotation.value() + "#" + annotation.method().toString();
          urlMap.put(endpoint, new HandlerDetails(details));
        }
      }
    }
  }

  private void autowireFields(List<String> classes)
      throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
      NoSuchMethodException, SecurityException, ClassNotFoundException {
    for (String className : classes) {
      Object bean = getBean(className);
      Class clazz = Class.forName(className);
      Field[] fields = clazz.getDeclaredFields();
      for (Field field : fields) {
        field.setAccessible(true);
        Autowired autowired = field.getAnnotation(Autowired.class);
        if (Objects.nonNull(autowired) && frameworkUtils.isBeanable(className)) {
          field.set(bean, getBean(field.getType().getName()));
        }
      }
    }
  }

  public HashMap<String, HandlerDetails> getUrlMap() {
    return urlMap;
  }

  public HashMap<String, Object> getBeanMap() {
    return beanMap;
  }

  public Object getBean(String className) {
    return beanMap.get(className);
  }
}
