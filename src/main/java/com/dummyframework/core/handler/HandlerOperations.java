package com.dummyframework.core.handler;

public class HandlerOperations {

  // RequestMapRegistry registry = RequestMapRegistry.getInstance();
  // Logger logger = new Logger(HandlerOperations.class);

  // public Object invoke(HttpServletRequest request, HttpServletResponse response)
  // throws IOException, ClassNotFoundException, ConverterException, ArrayBuilderException {
  // Request requestInfo = new Request(request);
  // logger.info("Incoming request for " + requestInfo.getUrl() + "#" + requestInfo.getMethod());
  // HandlerDetails handlerDetails = getHandlerDetails(requestInfo);
  // Object[] params = getMethodParams(handlerDetails, requestInfo);
  // return invoke(handlerDetails, params);
  // }

  // public Object invoke(HandlerDetails handlerDetails, Object[] params) {
  // Object result = null;
  // try {
  // Method method = handlerDetails.getCalledMethod();
  // Object object = handlerDetails.getComponent().getBean();
  // logger.info("Invoking method \"" + method.getName() + "\"");
  // result = method.invoke(object, params);
  // } catch (Exception e) {
  // e.printStackTrace();
  // }
  // return result;
  // }

  // /*
  // * Tries to convert same payload to diffrent object is multiple {@RequestBody}
  // * annotations are found.
  // */
  // private Object[] getMethodParams(HandlerDetails details, Request request)
  // throws ClassNotFoundException, ConverterException, ArrayBuilderException {
  // Method calledMethod = details.getCalledMethod();
  // Parameter[] params = calledMethod.getParameters();
  // Object[] objects = new Object[params.length];
  // for (int i = 0; i < params.length; i++) {
  // RequestBody annotation = params[i].getAnnotation(RequestBody.class);
  // boolean isRequestBody = (annotation != null);
  // if (isRequestBody) {
  // TypeInfo info = new TypeInfo(params[i].getParameterizedType());
  // Converter converter = info.getConverter();
  // objects[i] = converter.convert(info, request.getPayload());
  // } else
  // objects[i] = null;
  // // TDOD : make @RequestParam
  // }
  // return objects;
  // }

  // private HandlerDetails getHandlerDetails(Request requestInfo) {
  // return registry.get(requestInfo);
  // }

}
