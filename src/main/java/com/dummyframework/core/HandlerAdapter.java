package com.dummyframework.core;

import static com.dummyframework.utils.FrameworkUtils.getPrimitiveClass;
import static com.dummyframework.utils.FrameworkUtils.isJavaObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.dummyframework.annotations.RequestBody;
import com.dummyframework.annotations.RequestMapping;
import com.dummyframework.deserialize.TypeInfo;
import com.dummyframework.deserialize.converters.Converter;
import com.dummyframework.exception.AppContextException;
import com.dummyframework.exception.NoPatternMatchedException;
import com.dummyframework.utils.Constants;
import com.dummyframework.utils.FrameworkUtils;
import com.dummyframework.utils.ResolveParams;

public class HandlerAdapter {

  private WebApplicationContext context;
  private ResolveParams resolveParams;
  private FrameworkUtils frameworkUtils;

  public HandlerAdapter(WebApplicationContext context) throws ClassNotFoundException {
    this.context = context;
    this.resolveParams = new ResolveParams();
    this.frameworkUtils = new FrameworkUtils();
  }

  public Object invokeMethod(HttpServletRequest request, HttpServletResponse response) throws IOException {

    Request requestInfo = new Request(request);

    return null;

    // HandlerMapper handlerMapper = new HandlerMapper();
    // HashMap<String, Object> method =
    // handlerMapper.findMethodForUrl(context.getUrlMap(), request);
    // Parameter[] params = (Parameter[]) method.get("params");
    // HashMap<String, Object> payload = FrameworkUtils.extractPayload(request);
    // List<Object> inputParamList = new ArrayList<Object>();
    // Class[] paramClassType = new Class[params.length];
    // getParamObjects(params, paramClassType, inputParamList, payload);
    // Object[] inputParamArray = new Object[inputParamList.size()];
    // inputParamArray = inputParamList.toArray();
    // Object classObject = context.getBean(method.get("class").toString());
    // Method classMethod =
    // classObject.getClass().getDeclaredMethod((String) method.get("method"),
    // paramClassType);
    // String returnType = classMethod.getReturnType().getName();
    // treatResponse(response, classMethod);
    // if (returnType.equals("void")) {
    // classMethod.invoke(classObject, inputParamArray);
    // return null;
    // } else {
    // Object returnValue = classMethod.invoke(classObject, inputParamArray);
    // request.setAttribute(Constants.RESOLVE,
    // frameworkUtils.isResolvable(classObject, classMethod));
    // return returnValue;
    // }
  }

  private void treatResponse(HttpServletResponse response, Method method) {
    RequestMapping annotation = method.getDeclaredAnnotation(RequestMapping.class);
    String contentType = annotation.produces();
    if (!contentType.equals(""))
      response.setContentType(contentType);
  }

  public void getParamObjects(Parameter[] params, Class[] paramClassType, List<Object> inputParamList,
      HashMap<String, Object> payload) throws NoSuchMethodException, SecurityException, InstantiationException,
      IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoPatternMatchedException {
    int paramCount = 0;
    for (Parameter param : params) {
      paramClassType[paramCount] = (param.getType().isPrimitive()) ? getPrimitiveClass(param.getType().getName())
          : param.getType();
      RequestBody annotation = param.getDeclaredAnnotation(RequestBody.class);
      boolean isRequestBody = (annotation != null);
      if (isRequestBody) {
        String tag = annotation.tag();
        Object object = null;
        if (!isJavaObject(param)) {
          HashMap<String, Object> paramValue = (HashMap<String, Object>) payload.get(tag);
          object = resolveParams.getParameterObject(param, paramValue);
        } else {
          object = payload.get(tag);
        }
        inputParamList.add(object);
      }
      paramCount++;
    }
  }
}
