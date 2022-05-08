package com.dummyframework.core.bean;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import com.dummyframework.annotations.Config;
import com.dummyframework.annotations.Controller;
import com.dummyframework.annotations.Service;
import com.dummyframework.utils.BeanType;

/**
 * Stores the data used by @BeanFactory to create bean.
 */
public class BeanDefinition {

  private String packageName;

  private String className;

  private BeanType beanType;

  private Constructor<?> noParamConstructor;

  private List<Constructor<?>> parameterizedConstructors = new ArrayList<>();

  private Method factoryMethod;

  private boolean hasFactoryMethod = false;

  private boolean hasParameterizedConstructors = false;

  private List<Method> methods = new ArrayList<>();

  private List<Field> fields = new ArrayList<>();

  private Class<?>[] implementedInterfaces;

  public String getClassName() {
    return className;
  }

  public void setClassName(String className) {
    this.className = className;
  }

  public Constructor<?> getNoParamConstructor() {
    return noParamConstructor;
  }

  public void setNoParamConstructor(Constructor<?> noParamConstructor) {
    this.noParamConstructor = noParamConstructor;
  }

  public List<Constructor<?>> getParameterizedConstructors() {
    return parameterizedConstructors;
  }

  public void addParameterizedConstructor(Constructor<?> parameterizedConstructor) {
    hasParameterizedConstructors = true;
    this.parameterizedConstructors.add(parameterizedConstructor);
  }

  public Method getFactoryMethod() {
    return factoryMethod;
  }

  public void setFactoryMethod(Method factoryMethod) {
    this.factoryMethod = factoryMethod;
    this.hasFactoryMethod = true;
  }

  public List<Method> getMethods() {
    return methods;
  }

  public void addMethod(Method method) {
    this.methods.add(method);
  }

  public List<Field> getFields() {
    return fields;
  }

  public void addField(Field field) {
    this.fields.add(field);
  }

  public Class<?>[] getImplementedInterfaces() {
    return implementedInterfaces;
  }

  public void setImplementedInterfaces(Class<?>[] implementedInterfaces) {
    this.implementedInterfaces = implementedInterfaces;
  }

  public BeanType getBeanType() {
    return beanType;
  }

  public void setBeanType(Class<?> clazz) {
    if (clazz == Config.class) {
      this.beanType = BeanType.CONFIGURATION;
    } else if (clazz == Controller.class) {
      this.beanType = BeanType.CONTROLLER;
    } else if (clazz == Service.class) {
      this.beanType = BeanType.SERVICE;
    }
  }

  public String getPackageName() {
    return packageName;
  }

  public void setPackageName(String packageName) {
    this.packageName = packageName;
  }

  public boolean hasFactoryMethod() {
    return hasFactoryMethod;
  }

  public boolean hasParameterizedConstructors() {
    return hasParameterizedConstructors;
  }
}
