package com.dummyframework.core;

import java.util.ArrayList;
import java.util.List;

import com.dummyframework.core.bean.BeanOperations;
import com.dummyframework.core.request.RequestResolver;
import com.dummyframework.exception.AppContextException;
import com.dummyframework.logger.Logger;

public class ApplicationContext {

    Logger logger = new Logger(ApplicationContext.class);
    BeanOperations beanOperations = new BeanOperations();
    RequestResolver requestResolver = new RequestResolver();

    public ApplicationContext(List<String> scannedClasses) throws ClassNotFoundException, AppContextException {
        List<Class<?>> classes = getClasses(scannedClasses);
        try {
            beanOperations.registerBean(classes);
            requestResolver.resolve(classes);
            logger.info("Application Context started.");
        } catch (Exception e) {
            throw new AppContextException("Cannot start App Context.");
        }
    }

    private List<Class<?>> getClasses(List<String> scannedClass) throws ClassNotFoundException {
        List<Class<?>> classes = new ArrayList<>();
        for (String className : scannedClass) {
            classes.add(toClass(className));
        }
        return classes;
    }

    private Class<?> toClass(String className) throws ClassNotFoundException {
        return Class.forName(className);
    }

}
