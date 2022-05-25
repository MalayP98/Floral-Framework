package com.dummyframework.core.bean;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import com.dummyframework.annotations.BeanName;
import com.dummyframework.logger.Logger;

public abstract class AbstractBeanFactory {

  private static Logger LOG = new Logger(AbstractBeanFactory.class);

  private final BeanDefinitionRegistry beanDefinitionRegistry =
      BeanDefinitionRegistry.getInstance();

  private final BeanRegistry beanRegistry = BeanRegistry.getInstance();

  private DependencyInjection dependencyInjection = new DependencyInjection(this);

  /**
   * Searches the bean registry for a bean with name 'withName', if such bean is present then it is
   * returned else new bean is created.
   * 
   * Uses class for creating bean. First searches the bean defination from the bean factory. If
   * present then uses this bean defination to create bean.
   * 
   * @throws Exception
   */
  public Object createBean(Class<?> clazz, String withName) throws Exception {
    if (beanRegistry.hasBean(withName)) {
      return beanRegistry.getBean(withName);
    }
    BeanDefinition definition = beanDefinitionRegistry.getBeanDefinition(clazz);
    if (Objects.nonNull(definition)) {
      List<String> beanNames = createBean(definition);
      if (withName.isEmpty()) {
        return beanRegistry.getBean(definition.getComponentName());
      } else {
        for (String beanName : beanNames) {
          if (beanName.equals(withName))
            return beanRegistry.getBean(beanName);
        }
      }
      throw new Exception("No bean with name '" + withName + "'  found.");
    }
    LOG.error("No definition found for class '" + clazz.getSimpleName() + "'.");
    return null;
  }

  /**
   * Takes bean defination and create object using this. Then autowires the fields present in this
   * object. Thus completing bean creation.
   * 
   * @return - list of bean names which are formed for this definition. Many beans can be formed for
   *         same bean defintion.
   */
  public List<String> createBean(BeanDefinition definition) {
    List<String> beanNames = new ArrayList<>();
    try {
      beanNames = createObject(definition);
      for (String beanName : beanNames) {
        dependencyInjection.performCompleteInjection(beanRegistry.getBean(beanName), definition);
      }
    } catch (Exception e) {
      LOG.error("Cannot instantiate bean with name '" + definition.getPackageName() + "."
          + definition.getClassName() + "'");
    }
    return beanNames;
  }

  /**
   * Creates the object first by using the factory methods. Bean is created by all factory methods
   * available and then constructors is use to create the bean is possible. All the beans created
   * are save into the bean registry. Example for multiple beans for same definition,
   * 
   * <pre>
   * &#64;Service
   * class Model {
   *   // body
   * }
   * 
   * &#64;Config
   * class ConfigurationClass {
   * 
   *   &#64;Dependency
   *   public Model fun1() {
   *     // method body
   *     return model;
   *   }
   * 
   *   @Dependency
   *   public Model fun2() {
   *     // method body
   *     return model;
   *   }
   * }
   * </pre>
   * 
   * In this example three bean will be create, 'model' form Model class,'fun1' from fun1 method
   * 'fun2' from fun2 method.
   */
  private List<String> createObject(BeanDefinition beanDefinition) throws IllegalAccessException,
      IllegalArgumentException, InvocationTargetException, Exception {
    List<String> beanNames = new ArrayList<>();
    if (beanDefinition.hasFactoryMethod()) {
      beanNames = createObjectWithFactoryMethod(beanDefinition.getFactoryMethods());
    }
    if (beanDefinition.isComponent()) {
      String componentName = beanDefinition.getComponentName();
      if (beanDefinition.hasParameterizedConstructors()) {
        beanRegistry.add(componentName, createObjectWithParameterizedConstructor(
            beanDefinition.getParameterizedConstructors()));
      } else {
        Constructor<?> nonParameterizedConstructor = beanDefinition.getNoParamConstructor();
        beanRegistry.add(componentName, nonParameterizedConstructor.newInstance());
      }
      beanNames.add(componentName);
    }
    return beanNames;
  }

  /**
   * Fetches parametrized constructor with largest number of parameters. If found, checks if all the
   * required params are present or not. If all params are not found it returns null.
   */
  private Constructor<?> fetchConstructor(List<Constructor<?>> parameterizedConstructors) {
    Constructor<?> maxArgumentConstructor = null;
    for (Constructor<?> parameterizedConstructor : parameterizedConstructors) {
      if (maxArgumentConstructor == null || maxArgumentConstructor
          .getParameterCount() < parameterizedConstructor.getParameterCount()) {
        maxArgumentConstructor = parameterizedConstructor;
      }
    }

    Class<?>[] paramTypes = maxArgumentConstructor.getParameterTypes();
    for (Class<?> clazz : paramTypes) {
      if (!beanDefinitionRegistry.hasBeanDefinition(beanDefinitionRegistry.generateBeanName(clazz)))
        return null;
    }
    return maxArgumentConstructor;
  }

  /**
   * Takes a method and invoke it to create a bean, to achieve this it first creates the bean for
   * it's parent class and then uses that bean to further call the method. Puts the name of the
   * created bean into a list which is return at the end.
   */
  private List<String> createObjectWithFactoryMethod(List<Method> factoryMethods)
      throws IllegalAccessException, IllegalArgumentException, InvocationTargetException,
      Exception {
    List<String> beanNames = new ArrayList<>();
    for (Method factoryMethod : factoryMethods) {
      Object parentBean = createBean(factoryMethod.getDeclaringClass(), "");
      String beanName = "";
      if (factoryMethod.isAnnotationPresent(BeanName.class)) {
        beanName = factoryMethod.getAnnotation(BeanName.class).name();
      } else {
        beanName = factoryMethod.getName();
      }
      beanRegistry.add(beanName, factoryMethod.invoke(parentBean,
          dependencyInjection.getMethodDependencies(factoryMethod)));
      beanNames.add(beanName);
    }
    return beanNames;
  }

  /**
   * Uses parameterized constructor to create the bean for the class if present.
   */
  private Object createObjectWithParameterizedConstructor(
      List<Constructor<?>> parameterizedConstructors) throws InstantiationException,
      IllegalAccessException, IllegalArgumentException, InvocationTargetException, Exception {
    Constructor<?> parameterizedConstructor = fetchConstructor(parameterizedConstructors);
    if (parameterizedConstructor != null) {
      return parameterizedConstructor
          .newInstance(dependencyInjection.getConstructorDependencies(parameterizedConstructor));
    }
    return null;
  }
}
