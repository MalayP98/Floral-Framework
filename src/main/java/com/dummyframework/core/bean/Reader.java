package com.dummyframework.core.bean;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.dummyframework.annotations.Autowired;
import com.dummyframework.annotations.Config;
import com.dummyframework.annotations.Controller;
import com.dummyframework.annotations.Dependency;
import com.dummyframework.annotations.Service;


// kind of builder class for BeanDefinition
public class Reader {

  private BeanDefinition beanDefinition;

  protected final Class<?>[] beanableAnnotations = {Config.class, Controller.class, Service.class};

  public BeanDefinition read(Class<?> clazz) {
    return read(clazz, true);
  }

  private BeanDefinition read(Class<?> clazz, boolean createIfBean) {
    Class<?> beanableAnnotation = getBeanableAnnotation(clazz);
    if (beanableAnnotation == null && createIfBean) {
      return null;
    }
    this.beanDefinition = new BeanDefinition();
    this.beanDefinition.setBeanType(beanableAnnotation);
    // create a configuration metadata object or bean defination object.
    readFields(clazz);
    readConstructors(clazz);
    readMethods(clazz);
    return beanDefinition;
  }

  // configuration matadata
  @SuppressWarnings("rawtypes")
  private Class<?> getBeanableAnnotation(Class<?> clazz) {
    for (Class ann : beanableAnnotations) {
      if (clazz.isAnnotationPresent(ann)) {
        return ann;
      }
    }
    return null;
  }

  // will read user defined constructor. constructor with @autowired or with 0 param will be
  // considered.
  private void readConstructors(Class<?> clazz) {
    Constructor<?>[] userDefinedConstructors = clazz.getDeclaredConstructors();
    for (Constructor<?> constructor : userDefinedConstructors) {
      if (constructor.getParameterCount() == 0) {
        if (this.beanDefinition.getNoParamConstructor() != null)
          this.beanDefinition.setNoParamConstructor(constructor);
        continue;
      }
      if (constructor.isAnnotationPresent(Autowired.class)) {
        this.beanDefinition.addParameterizedConstructor(constructor);
      }
    }
  }

  private void readMethods(Class<?> clazz) {
    Method[] userDefinedMethods = clazz.getDeclaredMethods();
    for (Method method : userDefinedMethods) {
      if (method.isAnnotationPresent(Autowired.class)
          || method.isAnnotationPresent(Dependency.class)) {
        this.beanDefinition.addMethod(method);
        if (method.isAnnotationPresent(Dependency.class)
            && this.beanDefinition.getBeanType() == Config.class) {
          if (method.getReturnType().equals(Void.TYPE)) {
            // throw exception, bean method cannot have void retrun type.
          }
          BeanDefinition beanMethodDefinition = read(method.getReturnType(), false);
          // save this beanMethodDefination to the registry or somewhere.
        }
      }
    }
  }

  // will read only autowired fields
  private void readFields(Class<?> clazz) {
    Field[] allFields = clazz.getDeclaredFields();
    for (Field field : allFields) {
      if (field.isAnnotationPresent(Autowired.class)) {
        this.beanDefinition.addField(field);
      }
    }
  }
}
