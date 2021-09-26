package com.dummyframework.core;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.dummyframework.annotations.RequestBody;
import com.dummyframework.deserialize.TypeInfo;
import com.dummyframework.deserialize.builders.ArrayBuilderException;
import com.dummyframework.deserialize.converters.Converter;
import com.dummyframework.deserialize.converters.ConverterException;

public class HandlerOperations {

    private WebApplicationContext context;

    public HandlerOperations(WebApplicationContext context) {
        this.context = context;
    }

    public Object invoke(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ClassNotFoundException, ConverterException, ArrayBuilderException {
        Request requestInfo = new Request(request);
        HandlerDetails handlerDetails = getHandlerDetails(requestInfo);
        Object[] params = getMethodParams(handlerDetails, requestInfo);
        return invoke(handlerDetails, params);
    }

    public Object invoke(HandlerDetails handlerDetails, Object[] objects) {
        Object result = null;
        try {
            result = handlerDetails.getCalledMethod().invoke(handlerDetails.getComponent(), objects);
        } catch (Exception e) {
            // Add logger for exception.
        }
        return result;
    }

    /*
     * Tries to convert same paayload to diffrent object is multiple {@RequestBody}
     * annotations are found.
     */
    private Object[] getMethodParams(HandlerDetails details, Request request)
            throws ClassNotFoundException, ConverterException, ArrayBuilderException {
        System.out.println(details);
        Method calledMethod = details.getCalledMethod();
        Parameter[] params = calledMethod.getParameters();
        Object[] objects = new Object[params.length];
        for (int i = 0; i < params.length; i++) {
            System.out.println(params[i].getName());
            RequestBody annotation = params[i].getAnnotation(RequestBody.class);
            boolean isRequestBody = (annotation != null);
            if (isRequestBody) {
                TypeInfo info = new TypeInfo(params[i].getParameterizedType());
                Converter converter = info.getConverter();
                objects[i] = converter.convert(info, request.getPayload());
            } else
                objects[i] = null;
            // TDOD : make @RequestParam
        }
        return objects;
    }

    private HandlerDetails getHandlerDetails(Request requestInfo) {
        String key = "/login" + "#" + requestInfo.getMethod();
        HashMap<String, HandlerDetails> urlMap = context.getUrlMap();
        return urlMap.get(key);
    }

}
