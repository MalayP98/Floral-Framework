package com.dummyframework.core.bean;

import java.util.HashMap;
import java.util.Map;
import com.dummyframework.logger.Logger;

public class BeanDefinitionRegistry {

  Logger LOG = new Logger(getClass());

  private static BeanDefinitionRegistry INSTANCE = null;

  private BeanDefinitionRegistry() {
  }

  public static BeanDefinitionRegistry getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new BeanDefinitionRegistry();
    }
    return INSTANCE;
  }

  private Map<String, BeanDefinition> beanDefinitionRegistry = new HashMap<>();

  private String generateBeanName(BeanDefinition definition) {
    return definition.getPackageName() + "." + definition.getClassName();
  }

  public String generateBeanName(Class<?> clazz) {
    LOG.info(
        "Bean name for " + clazz + " = " + clazz.getPackageName() + " + " + clazz.getSimpleName());
    return clazz.getPackageName() + "." + clazz.getSimpleName();
  }

  public void addToDefinitions(BeanDefinition definition) throws Exception {
    String beanName = generateBeanName(definition);
    if (hasBeanDefinition(beanName)) {
      throw new Exception("Bean with name '" + beanName + "' already present");
    }
    beanDefinitionRegistry.put(beanName, definition);
  }

  public int beanCount() {
    return beanDefinitionRegistry.size();
  }

  public boolean hasBeanDefinition(String beanName) {
    return beanDefinitionRegistry.containsKey(beanName);
  }

  public BeanDefinition getBeanDefinition(Class<?> clazz) {
    return getBeanDefinition(generateBeanName(clazz));
  }

  public BeanDefinition getBeanDefinition(String beanName) {
    if (!hasBeanDefinition(beanName)) {
      LOG.info("No bean definition found with name '" + beanName + "'");
      return null;
    }
    return beanDefinitionRegistry.get(beanName);
  }


  public void show() {
    System.out.println("\n\n\n ");
    for (Map.Entry<String, BeanDefinition> entry : beanDefinitionRegistry.entrySet()) {
      System.out.println(entry.getKey() + " --> " + entry.getValue() + "\n");
    }
  }

}
