package com.dummyframework.core.bean;

import java.lang.reflect.Method;
import com.dummyframework.annotations.Config;
import com.dummyframework.annotations.Controller;
import com.dummyframework.annotations.Dependency;
import com.dummyframework.annotations.Service;

public class BeanGenerator {

    protected final Class<?>[] beanableAnnotations = { Config.class, Controller.class, Service.class };

    // creating bean for classes with beanable annotations
    public Bean createBean(Class<?> clazz) {
        if (isBeanable(clazz)) {
            return createBeanWithoutCheck(clazz);
        }
        return null;
    }

    // no check need for extension class like BeanOperation
    protected Bean createBeanWithoutCheck(Class<?> clazz) {
        BeanBuilder builder = new BeanBuilder();
        builder.setBean(createObject(clazz));
        builder.setClazz(clazz);
        // beanable annotation have names of their classes
        builder.setBeanName(clazz.getSimpleName().toLowerCase());
        return new Bean(builder);
    }

    // creating bean from method with @Dependency annotation
    public Bean createBean(Method method, Object component) {
        if (method.isAnnotationPresent(Dependency.class)) {
            return createBeanWithoutCheck(method, component);
        }
        return null;
    }

    protected Bean createBeanWithoutCheck(Method method, Object component) {
        BeanBuilder builder = new BeanBuilder();
        try {
            builder.setBean(method.invoke(component));
        } catch (Exception e) {
            // TODO: cannot create bean for method <method name>
        }
        builder.setBeanName((method.getAnnotation(Dependency.class).name() == null) ? method.getName()
                : method.getAnnotation(Dependency.class).name());
        builder.setClazz(method.getReturnType());
        return new Bean(builder);
    }

    @SuppressWarnings("unchecked")
    protected boolean isBeanable(Class<?> clazz) {
        for (Class ann : beanableAnnotations) {
            if (clazz.isAnnotationPresent(ann)) {
                return true;
            }
        }
        return false;
    }

    protected Object createObject(Class<?> clazz) {
        try {
            return clazz.getConstructor().newInstance();
        } catch (Exception e) {
            e.getStackTrace();
        }
        return null;
    }
}
