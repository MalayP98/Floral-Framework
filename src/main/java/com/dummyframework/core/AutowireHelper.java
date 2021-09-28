package com.dummyframework.core;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import com.dummyframework.annotations.Autowired;
import com.dummyframework.core.bean.Bean;
import com.dummyframework.core.bean.BeanRegistry;

public class AutowireHelper {

    BeanRegistry registry = BeanRegistry.getInstance();

    public void autowire(List<Bean> beans) throws IllegalArgumentException, IllegalAccessException {
        for (Bean bean : beans) {
            Class<?> clazz = bean.getClazz();
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                autowire(field, bean.getBean());
            }
        }
    }

    public void autowire(Field field, Object component) throws IllegalArgumentException, IllegalAccessException {
        if (field.isAnnotationPresent(Autowired.class)) {
            field.setAccessible(true);
            field.set(component, registry.getBean(getBeanName(field)));
        }
    }

    private String getBeanName(Field field) {
        Autowired annotation = field.getAnnotation(Autowired.class);
        if (!annotation.name().isEmpty()) {
            return annotation.name();
        }
        return field.getType().getSimpleName().toLowerCase();
    }
}
