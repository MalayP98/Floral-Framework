package com.dummyframework.core.bean;

import java.util.List;

public class BeanFactory extends AbstractBeanFactory {

  public void createBeans(List<Class<?>> classes) throws Exception {
    for (Class<?> clazz : classes) {
      createBean(clazz, "");
    }
  }
}
