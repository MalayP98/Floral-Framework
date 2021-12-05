package com.dummyframework.core.bean;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import com.dummyframework.annotations.Autowired;
import com.dummyframework.annotations.Config;
import com.dummyframework.annotations.Controller;
import com.dummyframework.annotations.Dependency;
import com.dummyframework.annotations.Service;
import com.dummyframework.logger.Logger;

// kind of builder class for BeanDefinition
public class Reader {

  protected final Class<?>[] beanableAnnotations = {Config.class, Controller.class, Service.class};

  private BeanDefinitionRegistry registry;

  private final Logger LOG = new Logger(getClass());

  public Reader() {
    this.registry = BeanDefinitionRegistry.getInstance();
  }

  public void register(List<Class<?>> classes) {
    for (Class<?> clazz : classes) {
      try {
        LOG.info("Reading " + clazz);
        registry.addToDefinitions(read(clazz));
      } catch (Exception e) {
        LOG.error("Cannot read bean to create definition.");
      }
    }
  }

  public BeanDefinition read(Class<?> clazz) throws Exception {
    return read(clazz, true);
  }

  private BeanDefinition read(Class<?> clazz, boolean createIfBean) throws Exception {
    Class<?> beanableAnnotation = getBeanableAnnotation(clazz);
    if (beanableAnnotation == null && createIfBean) {
      return null;
    }
    BeanDefinition beanDefinition = new BeanDefinition();
    beanDefinition.setBeanType(beanableAnnotation);
    beanDefinition.setClassName(clazz.getName());
    beanDefinition.setPackageName(clazz.getPackageName());
    beanDefinition.setImplementedInterfaces(clazz.getInterfaces());
    readFields(clazz, beanDefinition);
    readConstructors(clazz, beanDefinition);
    readMethods(clazz, beanDefinition);
    return beanDefinition;
  }

  @SuppressWarnings("rawtypes")
  private Class<?> getBeanableAnnotation(Class<?> clazz) {
    for (Class ann : beanableAnnotations) {
      if (clazz.isAnnotationPresent(ann)) {
        return ann;
      }
    }
    return null;
  }

  private void readConstructors(Class<?> clazz, BeanDefinition beanDefinition)
      throws NoSuchMethodException, SecurityException {
    Constructor<?>[] userDefinedConstructors = clazz.getDeclaredConstructors();
    for (Constructor<?> constructor : userDefinedConstructors) {
      if (constructor.getParameterCount() == 0) {
        if (beanDefinition.getNoParamConstructor() != null)
          beanDefinition.setNoParamConstructor(constructor);
      } else if (constructor.isAnnotationPresent(Autowired.class)) {
        beanDefinition.addParameterizedConstructor(constructor);
      }
    }
    if (beanDefinition.getNoParamConstructor() == null) {
      beanDefinition.setNoParamConstructor(clazz.getConstructor());
    }
  }

  private void readMethods(Class<?> clazz, BeanDefinition beanDefinition) throws Exception {
    Method[] userDefinedMethods = clazz.getDeclaredMethods();
    for (Method method : userDefinedMethods) {
      if (method.isAnnotationPresent(Dependency.class)
          && beanDefinition.getBeanType() == Config.class) {
        if (method.getReturnType().equals(Void.TYPE)) {
          continue;
        }
        BeanDefinition beanMethodDefinition = read(method.getReturnType(), false);
        method.setAccessible(true);
        beanMethodDefinition.setFactoryMethod(method);
        registry.addToDefinitions(beanMethodDefinition);
      } else if (method.isAnnotationPresent(Autowired.class)) {
        method.setAccessible(true);
        beanDefinition.addMethod(method);
      }
    }
  }

  private void readFields(Class<?> clazz, BeanDefinition beanDefinition) {
    Field[] allFields = clazz.getDeclaredFields();
    for (Field field : allFields) {
      if (field.isAnnotationPresent(Autowired.class)) {
        field.setAccessible(true);
        beanDefinition.addField(field);
      }
    }
  }
}
