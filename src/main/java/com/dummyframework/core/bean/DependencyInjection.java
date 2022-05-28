package com.dummyframework.core.bean;

import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.List;
import com.dummyframework.annotations.BeanName;

public class DependencyInjection {

  private AbstractBeanFactory beanFactory;

  private DependencyInjection() {

  }

  /**
   * Takes AbstractBeanFactory class to create beans for injection.
   */
  public DependencyInjection(AbstractBeanFactory beanFactory) {
    this.beanFactory = beanFactory;
  }

  /**
   * complete injection involves injecting fields and calling methods.
   * 
   * @param bean,       it is the object in which the fields are inject and methods are called.
   * @param definition, uses this definition for accessing fields and methods.
   */
  public void performCompleteInjection(Object bean, BeanDefinition definition)
      throws IllegalArgumentException, IllegalAccessException, Exception {
    List<Field> fields = definition.getFields();
    for (Field field : fields) {
      field.set(bean, getFieldDependency(field, findBeanName(field)[0]));
    }
    List<Method> methods = definition.getMethods();
    for (Method method : methods) {
      method.invoke(bean, getMethodDependencies(method));
    }
  }

  // uses @createParameterBeans() to get parameters beans required by the constructor.
  public Object[] getConstructorDependencies(Constructor<?> constructor) throws Exception {
    return createParameterBeans(constructor);
  }

  // uses @createParameterBeans() to get parameters beans required by the method.
  public Object[] getMethodDependencies(Method method) throws Exception {
    return createParameterBeans(method);
  }

  // Takes a single field and create it's bean if the field is not *static*.
  public Object getFieldDependency(Field field, String beanName) throws Exception {
    if (Modifier.isStatic(field.getModifiers())) {
      throw new Exception("Cannot inject into static field.");
    }
    Class<?> fieldType = field.getType();
    return beanFactory.createBean(fieldType, beanName);
  }

  /**
   * Constructor and Method both come under the same type, i.e @Executable.
   * 
   * This function take an executable to get all the parameter present in that executable and create
   * beans for all the parameters.
   * 
   * @return the parameter bean array which is then used to either invoke method or call
   *         constructor.
   * @throws Exception
   */
  private <T extends Executable> Object[] createParameterBeans(T executable) throws Exception {
    Class<?>[] params = executable.getParameterTypes();
    if (params.length == 0)
      return null;
    Object[] objects = new Object[params.length];
    String[] beanNames = findBeanName(executable);
    for (int i = 0; i < params.length; i++) {
      objects[i] = beanFactory.createBean(params[i], beanNames[i]);
    }
    return objects;
  }

  /**
   * Finds the name of the bean which are specified by the @BeanName annotation. If no bean name is
   * given then empty string is passed.
   * 
   * @param dependency - on which the bean name is searched for.
   * @return
   */
  private String[] findBeanName(Object dependency) {
    if (dependency instanceof Field) {
      Field field = (Field) dependency;
      String beanName = "";
      if (field.isAnnotationPresent(BeanName.class)) {
        beanName = field.getAnnotation(BeanName.class).name();
      }
      return new String[] {beanName};
    } else if (dependency instanceof Executable) {
      Executable executable = (Executable) dependency;
      int paramCount = executable.getParameterCount();
      String[] beanNames = new String[paramCount];
      Parameter[] params = executable.getParameters();
      for (int i = 0; i < paramCount; i++) {
        if (params[i].isAnnotationPresent(BeanName.class)) {
          beanNames[i] = params[i].getAnnotation(BeanName.class).name();
        } else
          beanNames[i] = "";
      }
      return beanNames;
    }
    return new String[0];
  }
}
