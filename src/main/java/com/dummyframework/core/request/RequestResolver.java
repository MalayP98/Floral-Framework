package com.dummyframework.core.request;

import java.lang.reflect.Method;
import java.util.List;
import com.dummyframework.annotations.Controller;
import com.dummyframework.annotations.RequestMapping;
import com.dummyframework.annotations.ResponseBody;
import com.dummyframework.core.Properties;
import com.dummyframework.core.handler.HandlerDetails;
import com.dummyframework.core.handler.HandlerDetailsBuilder;

public class RequestResolver {

    RequestMapRegistry registry = RequestMapRegistry.getInstance();

    private String APP_NAME = "APP_NAME";

    public void resolve(List<Class<?>> classes) {
        for (Class<?> clazz : classes) {
            if (clazz.isAnnotationPresent(Controller.class)) {
                addMappedMethods(clazz);
            }
        }
    }

    private void addMappedMethods(Class<?> clazz) {
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(RequestMapping.class)) {
                RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                HandlerDetailsBuilder details = new HandlerDetailsBuilder();
                details.setComponent(method.getDeclaringClass());
                details.setCalledMethod(method);
                details.setAtResponseBody(method.isAnnotationPresent(ResponseBody.class));
                registry.add(addAppName(requestMapping.value()), requestMapping.method(), new HandlerDetails(details));
            }
        }
    }

    private String addAppName(String url) {
        StringBuilder builder = new StringBuilder("/");
        builder.append(Properties.get(APP_NAME));
        builder.append(url);
        return builder.toString();
    }
}
