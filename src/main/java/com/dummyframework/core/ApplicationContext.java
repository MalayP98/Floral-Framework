package com.dummyframework.core;

import java.util.ArrayList;
import java.util.List;

import com.dummyframework.exception.AppContextException;

public class ApplicationContext {

    BeanOperations beanOperations = new BeanOperations();

    public ApplicationContext(List<String> scannedClasses) throws ClassNotFoundException, AppContextException {
        List<Class<?>> classes = getClasses(scannedClasses);
        try {
            beanOperations.registerBean(classes);
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
