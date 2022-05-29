package com.dummyframework.core.handler;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.dummyframework.annotations.PathVariable;
import com.dummyframework.annotations.QueryParameter;
import com.dummyframework.annotations.RequestBody;
import com.dummyframework.annotations.ResponseBody;

public class HandlerDetail {

  private Method handler;

  private String uri;

  // maps all the path variables names with the type of paramter.
  private Map<String, Type> pathVariables = new HashMap<>();

  // maps all the query parameter names with type of query parameter.
  private Map<String, Type> queryParameters = new HashMap<>();

  // keeps name of all the parameter as java sotres the names as argN.
  private List<String> parameterNames = new ArrayList<>();

  // type of @RequestBody parameter.
  private Type payload = null;

  private boolean sendResponse = false;

  public HandlerDetail(Method handler, String uri) {
    this.handler = handler;
    this.uri = uri;
    resolveHandlerParameters();
    setSendResponse();
  }

  public Method getHandler() {
    return handler;
  }

  public String getUri() {
    return uri;
  }

  public boolean canSendResponseToBody() {
    return this.sendResponse;
  }

  /**
   * Extracts names of parameter from the annotations like @PathVariable, @QueryParameter and map
   * them to the respsective type of parameter. Also store the names in parameterName list.
   * 
   * For payload type parameter, if multiple such parameters are present for example,
   * 
   * <pre>
   * public String handler(@RequestBody List<String> param1, @RequestBody int param2,....){ //function 
   * }
   * </pre>
   * 
   * The first parameter will always be chosen. For this example 'param1' will be used to map the
   * payload.
   */
  private void resolveHandlerParameters() {
    Parameter[] parameters = handler.getParameters();
    for (Parameter parameter : parameters) {
      String paramName = parameter.getName();
      if (parameter.isAnnotationPresent(PathVariable.class)) {
        PathVariable pathVariable = parameter.getAnnotation(PathVariable.class);
        pathVariables.put(pathVariable.name(), parameter.getParameterizedType());
        paramName = pathVariable.name();
      } else if (parameter.isAnnotationPresent(QueryParameter.class)) {
        QueryParameter queryParameter = parameter.getAnnotation(QueryParameter.class);
        queryParameters.put(queryParameter.name(), parameter.getParameterizedType());
        paramName = queryParameter.name();
      } else if (parameter.isAnnotationPresent(RequestBody.class)) {
        if (payload == null)
          payload = parameter.getParameterizedType();
      }
      parameterNames.add(paramName);
    }
  }

  /**
   * Decides wether this handler can write output in the response. It can only write the response
   * only when the method can return something and @RequestBody annotation is present on the method
   * or @RequestBody annotation is present on the parent class.
   */
  private void setSendResponse() {
    sendResponse =
        (handler.getReturnType() != Void.class && (handler.isAnnotationPresent(ResponseBody.class)
            || parentClass().isAnnotationPresent(ResponseBody.class)));
  }

  public Map<String, Type> getPathVariables() {
    return pathVariables;
  }

  public Map<String, Type> getQueryParameters() {
    return queryParameters;
  }

  public Type getPayload() {
    return payload;
  }

  public Class<?> parentClass() {
    return handler.getDeclaringClass();
  }

  public List<String> getParameterNames() {
    return parameterNames;
  }
}
