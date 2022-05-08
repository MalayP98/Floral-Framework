package com.dummyframework.core.bean;

import java.util.List;

public class BeanFactory extends AbstractBeanFactory {

  private void registerBeans(List<Class<?>> classes) {
    Reader reader = new Reader();
    reader.register(classes);
  }

  public void createBeans(List<Class<?>> classes) {
    registerBeans(classes);
    for (Class<?> clazz : classes) {
      createBean(clazz);
    }
  }
}
