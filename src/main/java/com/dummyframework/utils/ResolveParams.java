package com.dummyframework.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;
import com.dummyframework.exception.NoPatternMatchedException;

public class ResolveParams {

  public Object getParameterObject(Parameter param, HashMap<String, Object> paramValue)
      throws NoSuchMethodException, SecurityException, InstantiationException,
      IllegalAccessException, IllegalArgumentException, InvocationTargetException,
      NoPatternMatchedException {
    Class paramClass = param.getType();
    Constructor constructor = paramClass.getConstructor(null);
    Object paramObject = constructor.newInstance(null);
    Method[] methods = paramClass.getDeclaredMethods();
    HashMap<String, Method> setters = new HashMap<String, Method>();
    for (Method method : methods) {
      String setterName = null;
      try {
        setterName = FrameworkUtils.matchPattern(method.getName(), Constants.SETTER_METHOD, 1);
        setters.put(setterName.toLowerCase(), method);
      } catch (NoPatternMatchedException e) {
        System.out.println(e.getMessage());
      }
    }
    setParamField(setters, paramValue, paramObject);
    return paramObject;
  }

  private void setParamField(HashMap<String, Method> setters, HashMap<String, Object> paramValue,
      Object paramObject)
      throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    for (Map.Entry<String, Method> setter : setters.entrySet()) {
      Method method = setter.getValue();
      if (paramValue.containsKey(setter.getKey())) {
        Object value = null;
        Class paramType = (method.getParameterTypes())[0];
        if (paramType.isEnum())
          value = handelEnum(paramType, paramValue.get(setter.getKey()));
        else
          value = paramValue.get(setter.getKey());
        method.invoke(paramObject, value);
      }
    }
  }

  private Object handelEnum(Class paramType, Object object) {
    String value = (String) object;
    Object[] enumConstants = paramType.getEnumConstants();
    for (Object enumConstant : enumConstants) {
      if (enumConstant.toString().equalsIgnoreCase(value))
        return enumConstant;
    }
    return null;
  }
}
