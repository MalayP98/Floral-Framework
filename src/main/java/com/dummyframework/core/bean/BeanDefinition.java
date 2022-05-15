package com.dummyframework.core.bean;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import com.dummyframework.annotations.Config;
import com.dummyframework.annotations.Controller;
import com.dummyframework.annotations.Service;
import com.dummyframework.utils.ComponentType;

/**
 * Stores the data used by @BeanFactory to create bean.
 */
public class BeanDefinition {

  private String packageName;

  private String className;

  // Defines the type of component this bean is.
  private ComponentType beanType;

  // Stroes the name provided to the component. Can be provided by setting it inside a component
  // annotation.
  private String componentName;

  private Constructor<?> noParamConstructor;

  private List<Constructor<?>> parameterizedConstructors = new ArrayList<>();

  private List<Method> factoryMethods = new ArrayList<>();

  private boolean hasFactoryMethod = false;

  private boolean hasParameterizedConstructors = false;

  private List<Method> methods = new ArrayList<>();

  private List<Field> fields = new ArrayList<>();

  private Class<?>[] implementedInterfaces;

  // If this has @Primary annotation. If multiple beans are present with same then a primary bean is
  // searched.
  private boolean isPrimaryBean = false;

  // Tells if this bean definition has any of the component annotation provided in FrameworkUtil
  // class.
  private boolean isComponent = false;

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

  public List<Method> getFactoryMethods() {
    return factoryMethods;
  }

  public void addFactoryMethod(Method factoryMethod) {
    this.factoryMethods.add(factoryMethod);
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

  public ComponentType getBeanType() {
    return beanType;
  }

  public void setBeanType(Class<?> clazz) {
    if (Objects.isNull(clazz)) {
      this.beanType = ComponentType.NONE;
    } else {
      this.isComponent = true;
      if (clazz == Config.class) {
        this.beanType = ComponentType.CONFIGURATION;
      } else if (clazz == Controller.class) {
        this.beanType = ComponentType.CONTROLLER;
      } else if (clazz == Service.class) {
        this.beanType = ComponentType.SERVICE;
      }
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

  public boolean isPrimaryBean() {
    return isPrimaryBean;
  }

  public void setIsPrimaryBean(boolean isPrimaryBean) {
    this.isPrimaryBean = isPrimaryBean;
  }

  public String getComponentName() {
    return componentName;
  }

  public void setComponentName(String componentName) {
    this.componentName = componentName;
  }

  public boolean isComponent() {
    return this.isComponent;
  }

  @Override
  public String toString() {
    return "BeanDefinition [beanType=" + beanType + ", className=" + className + ", componentName="
        + componentName + ", factoryMethods=" + Arrays.toString(factoryMethods.toArray())
        + ", fields=" + fields + ", hasFactoryMethod=" + hasFactoryMethod
        + ", hasParameterizedConstructors=" + hasParameterizedConstructors
        + ", implementedInterfaces=" + Arrays.toString(implementedInterfaces) + ", isComponent="
        + isComponent + ", isPrimaryBean=" + isPrimaryBean + ", methods=" + methods
        + ", noParamConstructor=" + noParamConstructor + ", packageName=" + packageName
        + ", parameterizedConstructors=" + parameterizedConstructors + "]";
  }
}
