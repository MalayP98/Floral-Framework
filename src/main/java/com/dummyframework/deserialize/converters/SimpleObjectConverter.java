package com.dummyframework.deserialize.converters;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.dummyframework.deserialize.Separator;
import com.dummyframework.deserialize.TypeInfo;
import com.dummyframework.deserialize.builders.ArrayBuilderException;

public class SimpleObjectConverter implements Converter {

  Separator separator = new Separator();
  private final String PARENTHESIS = "\\((.*)\\)";
  private final String SET = "set";

  private String removeParenthesis(String content) throws ConverterException {
    Pattern pattern = Pattern.compile(PARENTHESIS);
    Matcher matcher = pattern.matcher(content);
    if (matcher.find()) {
      return matcher.group(1);
    }
    throw new ConverterException("Not a valid input.");
  }

  private Object createObject(Class<?> clazz)
      throws InstantiationException, IllegalAccessException, IllegalArgumentException,
      InvocationTargetException, NoSuchMethodException, SecurityException {
    return clazz.getConstructor().newInstance();
  }

  @Override
  public Object convert(TypeInfo info, String content)
      throws ConverterException, ArrayBuilderException {
    logger.info(
        "Converting \"" + content + "\" to " + info.getResolvedClass().getSimpleName() + ".class");
    Map<String, String> fieldValues = separator.toMap(removeParenthesis(content));
    return populateObject(info.getResolvedClass(), fieldValues);
  }

  private Object populateObject(Class<?> clazz, Map<String, String> fieldValues)
      throws ConverterException {
    Object object = null;
    try {
      object = createObject(clazz);
    } catch (Exception e) {
      throw new ConverterException("Object for " + clazz.getName() + "connot be created.");
    }
    if (object != null) {
      invokeSetters(clazz, object, fieldValues);
    }
    return object;
  }

  private void invokeSetters(Class<?> clazz, Object object, Map<String, String> fieldValues) {
    Map<String, Method> setters = getSetterMethods(clazz);
    for (Map.Entry<String, String> pair : fieldValues.entrySet()) {
      String key = pair.getKey();
      Method method = setters.get(key.toLowerCase());
      if (method != null) {
        try {
          method.invoke(object, convertField(method, pair.getValue()));
        } catch (Exception e) {
          logger.error(e.getMessage());
          // TODO: add error log with method name
        }
      }
    }
  }

  private Object convertField(Method method, String content) {
    Type type = method.getGenericParameterTypes()[0];
    try {
      TypeInfo info = new TypeInfo(type);
      Converter converter = info.getConverter();
      return converter.convert(info, content);
    } catch (Exception e) {
      return null;
      // TODO: add warning log.
    }
  }

  private Map<String, Method> getSetterMethods(Class<?> clazz) {
    Map<String, Method> setters = new HashMap<>();
    Method[] methods = clazz.getDeclaredMethods();
    for (Method method : methods) {
      String name = method.getName();
      if (name.substring(0, 3).equals(SET)) {
        setters.put(name.substring(3).toLowerCase(), method);
      }
    }
    return setters;
  }

}
