package com.dummyframework.core.bean;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class BeanDefinition {

  private String className;

  private Class<?> beanType;

  private Scope scope;

  private boolean primary;

  private String parentClass;

  private Constructor<?> noParamConstructor;

  private List<Constructor<?>> parameterizedConstructors = new ArrayList<>();

  private Method factoryMethod;

  private List<Method> methods = new ArrayList<>();

  private List<Field> fields = new ArrayList<>();

  private Class<?> implementedInterfaces;

  public String getClassName() {
    return className;
  }

  public void setClassName(String className) {
    this.className = className;
  }

  public Scope getScope() {
    return scope;
  }

  public void setScope(Scope scope) {
    this.scope = scope;
  }

  public boolean isPrimary() {
    return primary;
  }

  public void setPrimary(boolean primary) {
    this.primary = primary;
  }

  public String getParentClass() {
    return parentClass;
  }

  public void setParentClass(String parentClass) {
    this.parentClass = parentClass;
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
    this.parameterizedConstructors.add(parameterizedConstructor);
  }

  public Method getFactoryMethod() {
    return factoryMethod;
  }

  public void setFactoryMethod(Method factoryMethod) {
    this.factoryMethod = factoryMethod;
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

  public Class<?> getImplementedInterfaces() {
    return implementedInterfaces;
  }

  public void setImplementedInterfaces(Class<?> implementedInterfaces) {
    this.implementedInterfaces = implementedInterfaces;
  }

  public Class<?> getBeanType() {
    return beanType;
  }

  public void setBeanType(Class<?> beanType) {
    this.beanType = beanType;
  }
}
