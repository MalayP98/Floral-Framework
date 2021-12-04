package com.dummyframework.core.bean;

import java.util.HashMap;
import java.util.Map;

public class BeanDefinitionRegistry {
  private static BeanDefinitionRegistry INSTANCE = null;

  private BeanDefinitionRegistry() {
  }

  public static BeanDefinitionRegistry getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new BeanDefinitionRegistry();
    }
    return INSTANCE;
  }

  Map<String, BeanDefinition> beanDefinitionRegistry = new HashMap<>();
}
