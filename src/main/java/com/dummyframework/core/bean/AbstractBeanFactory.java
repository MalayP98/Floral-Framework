package com.dummyframework.core.bean;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import com.dummyframework.logger.Logger;

public abstract class AbstractBeanFactory {

  private static Logger LOG = new Logger(AbstractBeanFactory.class);

  private final BeanDefinitionRegistry beanDefinitionRegistry =
      BeanDefinitionRegistry.getInstance();

  DependencyInjection dependencyInjection = new DependencyInjection(this);

  /**
   * Uses class for creating bean. First searches the bean defination from the bean factory. If
   * present then uses this bean defination to create bean.
   */
  public Object createBean(Class<?> clazz) {
    LOG.info("Creating bean for " + clazz.getSimpleName());
    BeanDefinition definition = beanDefinitionRegistry.getBeanDefinition(clazz);
    if (definition == null) {
      return null;
    }
    return createBean(definition);
  }

  /**
   * Takes bean defination and create object using this. Then autowires the fields present in this
   * object. Thus completing bean creation.
   */
  Object createBean(BeanDefinition definition) {
    Object bean = null;
    try {
      bean = createObject(definition);
      dependencyInjection.performCompleteInjection(bean, definition);
    } catch (Exception e) {
      LOG.error("Cannot instantiate bean with name '" + definition.getPackageName() + "."
          + definition.getClassName() + "'");
    }
    return bean;
  }

  /**
   * Creates the object by either using factory method or constructor.
   */
  private Object createObject(BeanDefinition beanDefinition) throws IllegalAccessException,
      IllegalArgumentException, InvocationTargetException, Exception {
    if (beanDefinition.hasFactoryMethod()) {
      return createObjectWithFactoryMethod(beanDefinition.getFactoryMethod());
    }
    if (beanDefinition.hasParameterizedConstructors()) {
      return createObjectWithParameterizedConstructor(
          beanDefinition.getParameterizedConstructors());
    }
    Constructor<?> nonParameterizedConstructor = beanDefinition.getNoParamConstructor();
    return nonParameterizedConstructor.newInstance();
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
   * it's parent class and then uses that bean to further call the method.
   */
  private Object createObjectWithFactoryMethod(Method factoryMethod) throws IllegalAccessException,
      IllegalArgumentException, InvocationTargetException, Exception {
    Object parentBean = createBean(factoryMethod.getDeclaringClass());
    return factoryMethod.invoke(parentBean,
        dependencyInjection.getMethodDependencies(factoryMethod));
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
