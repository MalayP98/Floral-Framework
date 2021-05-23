package com.dummyframework.core;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.dummyframework.annotations.RequestBody;
import com.dummyframework.annotations.RequestMapping;
import com.dummyframework.exception.AppContextException;
import com.dummyframework.exception.NoPatternMatchedException;
import com.dummyframework.utils.FrameworkUtils;

public class HandlerAdapter {

  private WebApplicationContext context;

  public HandlerAdapter(WebApplicationContext context) {
    this.context = context;
  }

  public Object invokeMethod(HttpServletRequest request, HttpServletResponse response)
      throws ClassNotFoundException, NoSuchMethodException, SecurityException,
      IllegalAccessException, IllegalArgumentException, InvocationTargetException,
      NoPatternMatchedException, AppContextException, IOException, InstantiationException {
    HashMap<String, Object> method;
    HandlerMapper handlerMapper = new HandlerMapper();
    method = handlerMapper.findMethodForUrl(context.getUrlMap(), request);
    Parameter[] params = (Parameter[]) method.get("params");
    HashMap<String, Object> payload = FrameworkUtils.extractPayload(request);
    List<Object> inputParamList = new ArrayList<Object>();
    Class[] paramTypeClass = new Class[params.length];
    int paramCount = 0;
    for (Parameter param : params) {
      paramTypeClass[paramCount] = param.getType();
      RequestBody annotation = param.getDeclaredAnnotation(RequestBody.class);
      boolean isRequestBody = (annotation != null);
      if (isRequestBody) {
        String tag = annotation.tag();
        Object object = null;
        if (!isJavaObject(param)) {
          HashMap<String, Object> paramValue = (HashMap<String, Object>) payload.get(tag);
          object = getParameterObject(param, paramValue);
        } else {
          object = payload.get(tag);
        }
        inputParamList.add(object);
      }
      paramCount++;
    }
    Object[] inputParamArray = new Object[inputParamList.size()];
    inputParamArray = inputParamList.toArray();
    Object classObject = context.getBean(method.get("class").toString());
    for (Class clazz : paramTypeClass) {
    }
    Method classMethod =
        classObject.getClass().getDeclaredMethod((String) method.get("method"), paramTypeClass);
    String returnType = classMethod.getReturnType().getName();
    treatResponse(response, classMethod);
    if (returnType.equals("void")) {
      classMethod.invoke(classObject, inputParamArray);
      return null;
    } else {
      Object returnValue = classMethod.invoke(classObject, inputParamArray);
      return returnValue;
    }

  }

  private void treatResponse(HttpServletResponse response, Method method) {
    RequestMapping annotation = method.getDeclaredAnnotation(RequestMapping.class);
    String contentType = annotation.produces();
    if (!contentType.equals(""))
      response.setContentType(contentType);
  }

  private Object getParameterObject(Parameter param, HashMap<String, Object> paramValue)
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
        setterName = FrameworkUtils.matchPattern(method.getName(), FrameworkUtils.SETTER_METHOD, 1);
        setters.put(setterName.toLowerCase(), method);
      } catch (NoPatternMatchedException e) {
        System.out.println(e.getMessage());
      }
    }
    setParamField(setters, paramValue, paramObject);
    return paramObject;
  }

  private boolean isJavaObject(Parameter param) {
    String paramClassType = param.getType().getName();
    if (paramClassType.startsWith("java.") || paramClassType.startsWith("[Ljava."))
      return true;
    return false;
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
