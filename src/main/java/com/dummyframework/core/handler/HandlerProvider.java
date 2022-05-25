package com.dummyframework.core.handler;

import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.dummyframework.annotations.PathVariable;
import com.dummyframework.annotations.QueryParameter;
import com.dummyframework.annotations.RequestBody;
import com.dummyframework.core.bean.BeanRegistry;
import com.dummyframework.core.request.Request;
import com.dummyframework.deserialize.TypeInfo;
import com.dummyframework.deserialize.builders.ArrayBuilderException;
import com.dummyframework.deserialize.converters.ConverterException;
import com.dummyframework.logger.Logger;

public class HandlerProvider {

  private HandlerRegistry handlerRegistry = HandlerRegistry.getInstance();

  private BeanRegistry beanRegistry = BeanRegistry.getInstance();

  private Logger LOG = new Logger(getClass());

  public HandlerDetail getHandlerDetail(Request request) throws Exception {
    return handlerRegistry.getHandlerDetail(request.getUrl(), request.getMethod());
  }

  public Object invokeHandler(Request request) throws Exception {
    HandlerDetail handlerDetail = getHandlerDetail(request);
    Object[] parameters = getHandlerParameters(request, handlerDetail);
    Object response = handlerDetail.getHandler()
        .invoke(beanRegistry.getBean(handlerDetail.parentClass().getSimpleName()), parameters);
    if (handlerDetail.canSendResponseToBody())
      return response;
    return null;
  }

  private Object[] getHandlerParameters(Request request, HandlerDetail handlerDetail)
      throws ClassNotFoundException, ConverterException, ArrayBuilderException {
    Map<String, Object> requestParameters = getRequestParameter(request.getUrl(), handlerDetail);
    Map<String, Object> queryParameters =
        getQueryParameters(request.getQueryParams(), handlerDetail.getQueryParameters());
    Parameter[] parameters = handlerDetail.getHandler().getParameters();
    List<String> parameterNames = handlerDetail.getParameterNames();
    Object[] parameterObjects = new Object[parameterNames.size()];
    boolean payloadCandidateFound = false;
    for (int i = 0; i < parameterObjects.length; i++) {
      if (parameters[i].isAnnotationPresent(PathVariable.class)) {
        parameterObjects[i] = requestParameters.get(parameterNames.get(i));
      } else if (parameters[i].isAnnotationPresent(QueryParameter.class)) {
        parameterObjects[i] = queryParameters.get(parameterNames.get(i));
      } else if (parameters[i].isAnnotationPresent(RequestBody.class) && !payloadCandidateFound) {
        parameterObjects[i] = deserialize(handlerDetail.getPayload(), request.getPayload());
        payloadCandidateFound = true;
      }
    }
    return parameterObjects;
  }

  private Map<String, Object> getRequestParameter(String url, HandlerDetail handlerDetail)
      throws ConverterException, ArrayBuilderException, ClassNotFoundException {
    Map<String, Object> requestParameters = new HashMap<>();
    String[] actualPath = url.split("/");
    String[] mappedPath = handlerDetail.getUri().split("/");
    for (int i = 0; i < mappedPath.length; i++) {
      if (Path.isDynamic(mappedPath[i])) {
        String paramName = mappedPath[i].substring(1, mappedPath[i].length() - 1);
        requestParameters.put(paramName,
            deserialize(handlerDetail.getPathVariables().get(paramName), actualPath[i]));
      }
    }
    return requestParameters;
  }

  private Map<String, Object> getQueryParameters(Map<String, String> queryParameterValues,
      Map<String, Class<?>> queryParameterType)
      throws ClassNotFoundException, ConverterException, ArrayBuilderException {
    Map<String, Object> queryParameterObjects = new HashMap<>();
    for (Map.Entry<String, String> queryParameterValue : queryParameterValues.entrySet()) {
      queryParameterObjects.put(queryParameterValue.getKey(), deserialize(
          queryParameterType.get(queryParameterValue.getKey()), queryParameterValue.getValue()));
    }
    return queryParameterObjects;
  }

  private Object deserialize(Type type, String content)
      throws ConverterException, ArrayBuilderException, ClassNotFoundException {
    TypeInfo info = new TypeInfo(type);
    return info.getConverter().convert(info, content);
  }
}
