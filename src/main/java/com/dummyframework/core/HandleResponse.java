package com.dummyframework.core;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import org.json.JSONObject;
import com.dummyframework.exception.NoPatternMatchedException;
import com.dummyframework.utils.FrameworkUtils;

public class HandleResponse {

  public static Object handle(Object returnValue, String contentType)
      throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    if (contentType.equals("application/json")) {
      HashMap<String, Object> fieldValues;
      fieldValues = getFieldValue(returnValue);
      JSONObject jsonObject = new JSONObject(fieldValues);
      return jsonObject;
    }
    return returnValue;
  }

  private static HashMap<String, Object> getFieldValue(Object returnValue)
      throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    Class objectClass = returnValue.getClass();
    Method[] methods = objectClass.getDeclaredMethods();
    HashMap<String, Object> fieldValueMap = new HashMap<String, Object>();
    for (Method method : methods) {
      String getterName = null;
      try {
        getterName = FrameworkUtils.matchPattern(method.getName(), FrameworkUtils.GETTER_METHOD, 1);
        fieldValueMap.put(getterName.toLowerCase(), method.invoke(returnValue, null));
      } catch (NoPatternMatchedException e) {
        System.out.println(e.getMessage());
      }
    }
    return fieldValueMap;
  }

}
