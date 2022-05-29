package com.dummyframework.core.handler;

import java.lang.reflect.Method;
import java.util.List;
import com.dummyframework.annotations.RequestMapping;
import com.dummyframework.core.bean.BeanDefinition;
import com.dummyframework.core.bean.BeanDefinitionRegistry;
import com.dummyframework.utils.FrameworkUtils;
import com.dummyframework.utils.RequestMethod;

public class HandlerMapper {

  private static BeanDefinitionRegistry beanDefinitionRegistry =
      BeanDefinitionRegistry.getInstance();

  private static Path path = new Path();

  private static HandlerRegistry handlerRegistry = HandlerRegistry.getInstance();

  /**
   * Takes a list of classes and get bean definition for that class. From that bean definition
   * checks if the bean is a controller. If bean is a controller then map the methods in that class
   * to the request.
   * 
   * @param classes : checking every class if it is a controller or not.
   */
  public void map(List<Class<?>> classes) {
    for (Class<?> clazz : classes) {
      if (beanDefinitionRegistry.hasBeanDefinition(clazz)) {
        BeanDefinition definition = beanDefinitionRegistry.getBeanDefinition(clazz);
        if (FrameworkUtils.isControllerBean(definition.getBeanType())) {
          mapHandlerToRequest(clazz);
        }
      }
    }
  }

  /**
   * Gets all the methods of a class. For every method checks if that method is mapped to some
   * request or not. If it is mapped to a request then the request path is added to the Path class.
   * A key is created which is then mapped to a Handler object.
   * 
   * Key : path returned from Path object + "#" + the method type (like, GET, POST,....)
   * 
   * Value : Handler object which takes handler and path as parameter.
   */
  private void mapHandlerToRequest(Class<?> clazz) {
    Method[] methods = clazz.getMethods();
    for (Method method : methods) {
      if (method.isAnnotationPresent(RequestMapping.class)) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        String uri = requestMapping.value();
        RequestMethod requestMethod = requestMapping.method();
        handlerRegistry.addHandler(path.addPath(uri) + "#" + requestMethod.toString(),
            new HandlerDetail(method, uri));
      }
    }
  }
}
