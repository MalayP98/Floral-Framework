package com.dummyframework.core.bean;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import com.dummyframework.logger.Logger;

public abstract class AbstractBeanFactory {

  Logger LOG = new Logger(getClass());

  private final BeanDefinitionRegistry beanDefinitionRegistry =
      BeanDefinitionRegistry.getInstance();

  private InjectDependency injectDependency = new InjectDependency(this);

  public Object start(Class<?> clazz) {
    BeanDefinition definition = beanDefinitionRegistry.getBeanDefinition(clazz);
    if (definition == null) {
      return null;
    }
    Object bean = null;
    try {
      bean = createObject(definition);
      autowire(bean, definition);
    } catch (Exception e) {
      LOG.error("Cannot instantiate bean with name '" + definition.getPackageName() + "."
          + definition.getClassName() + "'");
    }
    return bean;
  }

  private Object createObject(BeanDefinition beanDefinition) throws InstantiationException,
      IllegalAccessException, IllegalArgumentException, InvocationTargetException, Exception {
    if (beanDefinition.hasFactoryMethod()) {
      Method factoryMethod = beanDefinition.getFactoryMethod();
      Object parentBean = start(factoryMethod.getDeclaringClass());
      return factoryMethod.invoke(parentBean, injectDependency.autowire(factoryMethod));
    }
    Constructor<?> parameterizedConstructor =
        fetchConstructor(beanDefinition.getParameterizedConstructors());
    if (parameterizedConstructor != null) {
      return parameterizedConstructor
          .newInstance(injectDependency.autowire(parameterizedConstructor));
    }
    Constructor<?> nonParameterizedConstructor = beanDefinition.getNoParamConstructor();
    return nonParameterizedConstructor.newInstance();
  }

  private Constructor<?> fetchConstructor(List<Constructor<?>> parameterizedConstructors) {
    Collections.sort(parameterizedConstructors, new Comparator<Constructor<?>>() {
      @Override
      public int compare(Constructor<?> constructorA, Constructor<?> constructorB) {
        if (constructorA.getParameterCount() >= constructorB.getParameterCount()) {
          return 1;
        }
        return -1;
      }
    });
    boolean constructorFound = true;
    for (Constructor<?> constructor : parameterizedConstructors) {
      Class<?>[] paramTypes = constructor.getParameterTypes();
      for (Class<?> clazz : paramTypes) {
        if (!beanDefinitionRegistry
            .hasBeanDefinition(beanDefinitionRegistry.generateBeanName(clazz))) {
          constructorFound = false;
          break;
        }
      }
      if (constructorFound) {
        return constructor;
      }
    }
    return null;
  }

  private void autowire(Object bean, BeanDefinition definition) throws Exception {
    List<Field> fields = definition.getFields();
    for (Field field : fields) {
      injectDependency.autowire(bean, field);
    }
    List<Method> methods = definition.getMethods();
    for (Method method : methods) {
      method.invoke(bean, injectDependency.autowire(method));
    }
  }
}
