package com.dummyframework.core.bean;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;
import com.dummyframework.annotations.Autowired;
import com.dummyframework.annotations.Dependency;
import com.dummyframework.annotations.Primary;
import com.dummyframework.logger.Logger;
import com.dummyframework.utils.FrameworkUtils;

public class Reader {

  private BeanDefinitionRegistry registry;

  private final Logger LOG = new Logger(getClass());

  public Reader() {
    this.registry = BeanDefinitionRegistry.getInstance();
  }

  /**
   * Takes a class from a list of classes and create bean definition for each one of them and add
   * them to registry. BeanDifinition for all the classes will be created irrespective of wether it
   * is a POJO or not. This is done so that if a bean is required for a POJO class we should have a
   * definition in the registry. For example, if a POJO class is autowired some where in a service
   * class, we won't be having a definition for that class and no bean can be created.
   */
  public void register(List<Class<?>> classes) {
    for (Class<?> clazz : classes) {
      try {
        BeanDefinition definition = read(clazz);
        if (Objects.nonNull(definition))
          registry.addToDefinitions(definition);
      } catch (Exception e) {
        LOG.error("Cannot read class to create definition for class "
            + FrameworkUtils.className(clazz) + ".");
        LOG.error(e.getMessage());
      }
    }
  }

  /**
   * First checks if bean is already present, and returns if in registry.
   * 
   * Populates bean definition with required information like, beanable annotation which it uses,
   * class name with package, all the interfaces that this class implements. Reads constructor,
   * methods and fields in this class, adds them into the definition if they are quilified.
   * 
   * @param clazz class for bean difinition is to be created
   * @return return BeanDefinition formed for the class
   * @throws Exception
   */
  private BeanDefinition read(Class<?> clazz) throws Exception {
    if (registry.hasBeanDefinition(clazz)) {
      return registry.getBeanDefinition(clazz);
    }
    Class<?> componentAnnotation = FrameworkUtils.getComponentClass(clazz);
    BeanDefinition beanDefinition = new BeanDefinition();
    if (Objects.nonNull(componentAnnotation)) {
      beanDefinition.setComponentName(FrameworkUtils.beanName(clazz));
    }
    beanDefinition.setBeanType(componentAnnotation);
    beanDefinition.setClassName(clazz.getSimpleName());
    beanDefinition.setPackageName(clazz.getPackageName());
    beanDefinition.setImplementedInterfaces(clazz.getInterfaces());
    beanDefinition.setIsPrimaryBean(clazz.isAnnotationPresent(Primary.class));
    readConstructors(clazz, beanDefinition);
    readFields(clazz, beanDefinition);
    readMethods(clazz, beanDefinition);
    return beanDefinition;
  }

  /**
   * Searches for all the constructor with parameters. All the constructors found will be added to
   * the bean definition. Also the constructor will no parameter, if found, will be added to bean
   * difinitaion. If no contructor is found then the constructor given by java is added to the bean
   * definition's no param constructor.
   */
  private void readConstructors(Class<?> clazz, BeanDefinition beanDefinition)
      throws NoSuchMethodException, SecurityException {
    Constructor<?>[] userDefinedConstructors = clazz.getDeclaredConstructors();
    for (Constructor<?> constructor : userDefinedConstructors) {
      if (constructor.getParameterCount() == 0 && beanDefinition.getNoParamConstructor() == null) {
        beanDefinition.setNoParamConstructor(constructor);
      } else if (constructor.isAnnotationPresent(Autowired.class)) {
        beanDefinition.addParameterizedConstructor(constructor);
      }
    }
    if (beanDefinition.getNoParamConstructor() == null) {
      beanDefinition.setNoParamConstructor(clazz.getConstructor());
    }
  }

  /**
   * If a method in the list has @Dependency annotation i.e. it provides bean and is present inside
   * Configuration class then the return type of the method is saved as bean definition. If this
   * condition is false then it is checked that if the method is Autowired or not, it will be added
   * to the definition only if it is Autowired.
   */
  private void readMethods(Class<?> clazz, BeanDefinition beanDefinition) throws Exception {
    Method[] userDefinedMethods = clazz.getDeclaredMethods();
    for (Method method : userDefinedMethods) {
      // make factory method if method has Dependency annotation and is inside a Config class.
      if (method.isAnnotationPresent(Dependency.class)
          && FrameworkUtils.isConfigurationBean(beanDefinition.getBeanType())
          && !method.getReturnType().equals(Void.TYPE)) {
        BeanDefinition beanMethodDefinition = read(method.getReturnType());
        method.setAccessible(true);
        beanMethodDefinition.addFactoryMethod(method);
        registry.addToDefinitions(beanMethodDefinition);
      } else if (method.isAnnotationPresent(Autowired.class)) {
        method.setAccessible(true);
        beanDefinition.addMethod(method);
      }
    }
  }

  // Checks all the fields, and checks if field has @Autowired annotation. If present then field is
  // added to bean definition.
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
