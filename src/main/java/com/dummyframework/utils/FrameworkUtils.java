package com.dummyframework.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import com.dummyframework.exception.NoPatternMatchedException;

public class FrameworkUtils {

  public static final String ANNOTATION_MATCHER =
      "[a-zA-Z@\\.]*\\.([A-Za-z]*)\\((([A-Za-z]*)=\"([A-Za-z./\\s]*)\",?\\s?)*\\)";

  public static final String REQUEST_REGEX =
      "\\/([A-Za-z-.]*)\\/([A-Za-z.]*)(\\/[A-Za-z]*)\\/?([A-Za-z?&0-9=]*)\\/?";

  public static final String METHOD_CLASS_SEPRATOR_REGEX = "([A-Za-z._]*)#{1}([A-Za-z_]*)";

  public static final String PAYLOAD_EXTRACTOR =
      "([A-Za-z0-9]*):{1}\\({1}([A-Za-z\"=0-9,.@|\\s]*)\\){1}";

  public static final String SETTER_METHOD = "set{1}([a-zA-Z_]*)";

  public static final String GETTER_METHOD = "get{1}([a-zA-Z_]*)";

  public static final String EXTRACT_LIST = "\\|{1}([A-Za-z,\\s]*)\\|{1}";

  public static final String REMOVE_QUOTED = "\"{1}([A-Za-z0-9_@.]*)\"{1}";

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
      stringBuilder.append(line + ";");
      line = reader.readLine();
    }
    return stringBuilder.toString();
  }

  public static HashMap<String, String> getInput(String line) throws NoPatternMatchedException {
    HashMap<String, String> extracted = new HashMap<String, String>();
    String tag = matchPattern(line, PAYLOAD_EXTRACTOR, 1);
    String input = matchPattern(line, PAYLOAD_EXTRACTOR, 2);
    extracted.put("tag", tag);
    extracted.put("input", input);
    return extracted;
  }

  public static HashMap<String, Object> extractPayload(HttpServletRequest request)
      throws IOException, NoPatternMatchedException {
    String body = getBody(request);
    String[] lines = body.split(";");
    HashMap<String, Object> tagMap = new HashMap<String, Object>();
    for (String line : lines) {
      if (line.isEmpty())
        continue;
      HashMap<String, String> extracted = getInput(line);
      String tag = extracted.get("tag");
      String input = extracted.get("input");
      if (!input.startsWith("|")) {
        String[] values = input.split(",\\s?");
        HashMap<String, Object> paramValueMap = new HashMap<String, Object>();
        for (String value : values) {
          String[] params_value = value.split("=");
          String paramName = matchPattern(params_value[0], REMOVE_QUOTED, 1);
          paramValueMap.put(paramName, appropriateType(params_value[1]));
        }
        tagMap.put(tag, paramValueMap);
      } else {
        String rawList = input.substring(1, input.length() - 1);
        List<Object> list = Arrays.asList(rawList.split(",\\s?"));
        for (int i = 0; i < list.size(); i++) {
          list.set(i, appropriateType((String) list.get(i)));
        }
        tagMap.put(tag, list);
      }
    }
    return tagMap;
  }

  private static Object appropriateType(String value) throws NoPatternMatchedException {
    if (value.startsWith("\""))
      return matchPattern(value, REMOVE_QUOTED, 1);
    else if (value.split("\\.{1}").length == 2) {
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

}
