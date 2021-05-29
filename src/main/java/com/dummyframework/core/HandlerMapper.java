package com.dummyframework.core;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import com.dummyframework.exception.AppContextException;
import com.dummyframework.exception.NoPatternMatchedException;
import com.dummyframework.utils.Constants;
import com.dummyframework.utils.FrameworkUtils;

public class HandlerMapper {

  public HashMap<String, Object> findMethodForUrl(HashMap<String, List<Object>> urlMap,
      HttpServletRequest request) throws NoPatternMatchedException, ClassNotFoundException,
      NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException,
      InvocationTargetException, AppContextException {
    HashMap<String, Object> method = new HashMap<String, Object>();
    String url =
        FrameworkUtils.matchPattern(request.getRequestURI(), Constants.REQUEST_REGEX, 3);
    String requestType = request.getMethod();
    List<Object> specifications = urlMap.get(url + "#" + requestType);
    String classMethod = (String) specifications.get(0);
    Parameter[] params = (Parameter[]) specifications.get(1);
    String className =
        FrameworkUtils.matchPattern(classMethod, Constants.METHOD_CLASS_SEPRATOR_REGEX, 1);
    String methodName =
        FrameworkUtils.matchPattern(classMethod, Constants.METHOD_CLASS_SEPRATOR_REGEX, 2);
    method.put("class", className);
    method.put("method", methodName);
    method.put("params", params);
    return method;
  }

}
