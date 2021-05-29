package com.dummyframework.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import com.dummyframework.exception.NoPatternMatchedException;

public class FrameworkUtils {

  public static String matchPattern(String str, String regex, int group)
      throws NoPatternMatchedException {
    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(str);
    if (matcher.find()) {
      return matcher.group(group);
    }
    throw new NoPatternMatchedException();
  }

  private static String getBody(HttpServletRequest request) throws IOException {
    StringBuilder stringBuilder = new StringBuilder();
    BufferedReader reader = request.getReader();
    String line = reader.readLine();
    while (line != null) {
      stringBuilder.append(line + Constants.SEMI_COLON);
      line = reader.readLine();
    }
    return stringBuilder.toString();
  }

  public static HashMap<String, String> getInput(String line) throws NoPatternMatchedException {
    HashMap<String, String> extracted = new HashMap<String, String>();
    String tag = matchPattern(line, Constants.PAYLOAD_EXTRACTOR, Constants.ONE);
    String input = matchPattern(line, Constants.PAYLOAD_EXTRACTOR, Constants.TWO);
    extracted.put(Constants.TAG, tag);
    extracted.put(Constants.INPUT, input);
    return extracted;
  }

  private static Object getTagValue(String input) throws NoPatternMatchedException {
    if (!input.startsWith(Constants.ARRAY_CONTAINER)) {
      String[] values = input.split(Constants.PARAM_SEPRATOR);
      HashMap<String, Object> tagValueMap = new HashMap<String, Object>();
      for (String value : values) {
        String[] params_value = value.split(Constants.EQUAL);
        String paramName = (!params_value[Constants.ZERO].equals(Constants.DOLLAR))
            ? matchPattern(params_value[Constants.ZERO], Constants.REMOVE_QUOTED, Constants.ONE)
            : params_value[Constants.ZERO];
        tagValueMap.put(paramName, appropriateType(params_value[Constants.ONE]));
      }
      return tagValueMap;
    } else {
      String rawList = input.substring(1, input.length() - 1);
      List<Object> list = Arrays.asList(rawList.split(Constants.PARAM_SEPRATOR));
      for (int i = 0; i < list.size(); i++) {
        list.set(i, appropriateType((String) list.get(i)));
      }
      return list;
    }
  }

  public static HashMap<String, Object> extractPayload(HttpServletRequest request)
      throws IOException, NoPatternMatchedException {
    String body = getBody(request);
    String[] lines = body.split(Constants.SEMI_COLON);
    HashMap<String, Object> tagMap = new HashMap<String, Object>();
    for (String line : lines) {
      if (line.isEmpty())
        continue;
      HashMap<String, String> extracted = getInput(line);
      String tag = extracted.get(Constants.TAG);
      String input = extracted.get(Constants.INPUT);
      tagMap.put(tag, getTagValue(input));
    }
    return tagMap;
  }

  private static Object appropriateType(String value) throws NoPatternMatchedException {
    if (value.startsWith(Constants.QUOTES))
      return matchPattern(value, Constants.REMOVE_QUOTED, Constants.ONE);
    else if (value.split(Constants.DECIMAL_CHECK).length == Constants.TWO) {
      try {
        float object = Float.parseFloat(value);
        return object;
      } catch (NumberFormatException e) {
        double object = Double.parseDouble(value);
        return object;
      }
    }
    try {
      int object = Integer.parseInt(value);
      return object;
    } catch (NumberFormatException e) {
      long object = Long.parseLong(value);
      return object;
    }
  }

  public static Object converter(Class clazz, Object parameter) {
    if (clazz == Integer.class || clazz == Integer.TYPE) {
      return Integer.parseInt((String) parameter);
    } else if (clazz == Long.class || clazz == Long.TYPE) {
      return Long.parseLong((String) parameter);
    }
    return parameter;
  }

  public static boolean isJavaObject(Parameter param) {
    String paramClassType = param.getType().getName();
    if (paramClassType.contains(Constants.JAVA))
      return true;
    return false;
  }
}
