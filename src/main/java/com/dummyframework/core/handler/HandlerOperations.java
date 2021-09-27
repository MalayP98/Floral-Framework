package com.dummyframework.core.handler;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.dummyframework.annotations.RequestBody;
import com.dummyframework.core.request.Request;
import com.dummyframework.core.request.RequestMapRegistry;
import com.dummyframework.deserialize.TypeInfo;
import com.dummyframework.deserialize.builders.ArrayBuilderException;
import com.dummyframework.deserialize.converters.Converter;
import com.dummyframework.deserialize.converters.ConverterException;

public class HandlerOperations {

    RequestMapRegistry registry = RequestMapRegistry.getInstance();

    public Object invoke(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ClassNotFoundException, ConverterException, ArrayBuilderException {
        Request requestInfo = new Request(request);
        HandlerDetails handlerDetails = getHandlerDetails(requestInfo);
        System.out.println("handler detail --> " + handlerDetails);
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
     * Tries to convert same payload to diffrent object is multiple {@RequestBody}
     * annotations are found.
     */
    private Object[] getMethodParams(HandlerDetails details, Request request)
            throws ClassNotFoundException, ConverterException, ArrayBuilderException {
        System.out.println(details);
        Method calledMethod = details.getCalledMethod();
        Parameter[] params = calledMethod.getParameters();
        Object[] objects = new Object[params.length];
        for (int i = 0; i < params.length; i++) {
            System.out.println("\n param name --> " + params[i].getType().getName());
            RequestBody annotation = params[i].getAnnotation(RequestBody.class);
            boolean isRequestBody = (annotation != null);
            if (isRequestBody) {
                System.out.println("param --> " + params[i].getType().getName() + "has requestbody annotation");
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
        System.out.println("\n\n --> " + requestInfo.getUrl());
        return registry.get(requestInfo);
    }

}
