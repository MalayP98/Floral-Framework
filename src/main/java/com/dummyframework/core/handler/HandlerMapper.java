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

  public void map(List<Class<?>> classes) {
    for (Class<?> clazz : classes) {
      if (beanDefinitionRegistry.hasBeanDefinition(clazz)) {
        BeanDefinition definition = beanDefinitionRegistry.getBeanDefinition(clazz);
        if (FrameworkUtils.isControllerBean(definition.getBeanType())) {
          mapHandlers(clazz);
        }
      }
    }
    handlerRegistry.display();
  }

  private void mapHandlers(Class<?> clazz) {
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
