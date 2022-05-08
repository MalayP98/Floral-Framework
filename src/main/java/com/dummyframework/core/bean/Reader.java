package com.dummyframework.core.bean;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import com.dummyframework.annotations.Autowired;
import com.dummyframework.annotations.Dependency;
import com.dummyframework.logger.Logger;
import com.dummyframework.utils.FrameworkUtils;

public class Reader {

  private BeanDefinitionRegistry registry;

  private final Logger LOG = new Logger(getClass());

  public Reader() {
    this.registry = BeanDefinitionRegistry.getInstance();
  }

  // Takes a class from a list of classes and create bean definition for each one of them and add
  // them to registry.
  public void register(List<Class<?>> classes) {
    for (Class<?> clazz : classes) {
      try {
        registry.addToDefinitions(read(clazz));
      } catch (Exception e) {
        LOG.error("Cannot read bean to create definition.");
      }
    }
  }

  // the bean is only created if the class is beanable.
  public BeanDefinition read(Class<?> clazz) throws Exception {
    return read(clazz, true);
  }

  /**
   * Populates bean definition with required information like, beanable annotation which it uses,
   * class name with package, all the interfaces that this class implements. Reads constructor,
   * methods and fields in this class, adds them into the definition if they are quilified.
   * 
   * @param clazz
   * @param createIfBeanable only create a bean definition if this param is true, else it returns
   *                         false. This parameter comes in handy especially when a bean definition
   *                         hs to be created for a class which has no beanable annotation, for
   *                         example a @Dependency method in a config class.
   * @return return BeanDefinition formed for the class
   * @throws Exception
   */
  private BeanDefinition read(Class<?> clazz, boolean createIfBeanable) throws Exception {
    Class<?> beanableAnnotation = FrameworkUtils.getBeanableAnnotation(clazz);
    if (beanableAnnotation == null && createIfBeanable) {
      return null;
    }
    BeanDefinition beanDefinition = new BeanDefinition();
    beanDefinition.setBeanType(beanableAnnotation);
    beanDefinition.setClassName(clazz.getSimpleName());
    beanDefinition.setPackageName(clazz.getPackageName());
    beanDefinition.setImplementedInterfaces(clazz.getInterfaces());
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
   * Configuration class then the return type of the method is saved as bean definition. Even if the
   * above condition is false, the method is added to the bean definition if it is having @Autowired
   * annotation because it can happen that the method is somthing like this,
   * 
   * <pre>
   * &#64;Config
   * public class ConfigClass {
   * 
   *   &#64;Dependency
   *   @Autowired
   *   public ModelA createModelA(ModelB modelB) {
   *     // some code
   *     return modelAObject;
   *   }
   * }
   * </pre>
   * 
   * In the above situation the createModelA() method will return a bean and also needs ModelB to
   * create ModelA so this method will also be added into bean definition.
   */
  private void readMethods(Class<?> clazz, BeanDefinition beanDefinition) throws Exception {
    Method[] userDefinedMethods = clazz.getDeclaredMethods();
    for (Method method : userDefinedMethods) {
      // make factory method if method has Dependency annotation and is inside a Config class.
      if (method.isAnnotationPresent(Dependency.class)
          && FrameworkUtils.isConfigurationBean(beanDefinition.getBeanType())
          && !method.getReturnType().equals(Void.TYPE)) {
        BeanDefinition beanMethodDefinition = read(method.getReturnType(), false);
        method.setAccessible(true);
        beanMethodDefinition.setFactoryMethod(method);
        registry.addToDefinitions(beanMethodDefinition);
      }
      if (method.isAnnotationPresent(Autowired.class)) {
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
