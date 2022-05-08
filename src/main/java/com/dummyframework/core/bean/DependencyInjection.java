package com.dummyframework.core.bean;

import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;

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
      field.set(bean, getFieldDependency(field));
    }
    List<Method> methods = definition.getMethods();
    for (Method method : methods) {
      method.invoke(bean, getMethodDependencies(method));
    }
  }

  // uses @createParameterBeans() to get parameters beans required by the constructor.
  public Object[] getConstructorDependencies(Constructor<?> constructor) {
    return createParameterBeans(constructor);
  }

  // uses @createParameterBeans() to get parameters beans required by the method.
  public Object[] getMethodDependencies(Method method) {
    return createParameterBeans(method);
  }

  // Takes a single field and create it's bean if the field is not *static*.
  public Object getFieldDependency(Field field) throws Exception {
    if (Modifier.isStatic(field.getModifiers())) {
      throw new Exception("Cannot inject into static field.");
    }
    Class<?> fieldType = field.getType();
    return beanFactory.createBean(fieldType);
  }

  /**
   * Constructor and Method both come under the same type, i.e @Executable.
   * 
   * This function take an executable to get all the parameter present in that executable and create
   * beans for all the parameters.
   * 
   * @return the parameter bean array which is then used to either invoke method or call
   *         constructor.
   */
  private <T extends Executable> Object[] createParameterBeans(T executable) {
    Class<?>[] params = executable.getParameterTypes();
    Object[] objects = new Object[params.length];
    for (int i = 0; i < params.length; i++) {
      objects[i] = beanFactory.createBean(params[i]);
    }
    return objects;
  }
}
