package com.dummyframework.core.bean;

import java.io.EOFException;
import java.lang.reflect.Executable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import com.dummyframework.logger.Logger;

public class InjectDependency {

  private AbstractBeanFactory beanFactory;

  Logger LOG = new Logger(getClass());

  protected InjectDependency() {

  }

  public InjectDependency(AbstractBeanFactory beanFactory) {
    this.beanFactory = beanFactory;
  }

  public void autowire(Object bean, Field field) throws Exception {
    if (Modifier.isStatic(field.getModifiers())) {
      throw new Exception("Cannot inject into static field.");
    }
    Class<?> fieldType = field.getType();
    field.set(bean, beanFactory.start(fieldType));
  }

  public <T extends Executable> Object[] autowire(T executable) throws Exception {
    Class<?>[] params = executable.getParameterTypes();
    Object[] objects = new Object[params.length];
    for (int i = 0; i < params.length; i++) {
      objects[i] = beanFactory.start(params[i]);
    }
    return objects;
  }
}
