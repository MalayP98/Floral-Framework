package com.dummyframework.core;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.dummyframework.annotations.Config;
import com.dummyframework.annotations.Dependency;
import com.dummyframework.exception.BeanRegistryException;

public class BeanOperations extends BeanGenerator {

    BeanRegistry registry = BeanRegistry.getInstance();
    AutowireHelper helper = new AutowireHelper();

    private List<Class<?>> getBeanableClasses(List<Class<?>> scannedClasses) {
        List<Class<?>> beanableClasses = new ArrayList<>();
        for (Class<?> scannedClass : scannedClasses) {
            if (isBeanable(scannedClass)) {
                beanableClasses.add(scannedClass);
            }
        }
        return beanableClasses;
    }

    private void runDependencyMethods(Bean bean) throws BeanRegistryException {
        Class<?> clazz = bean.getClass();
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(Dependency.class)) {
                registry.addBean(createBeanWithoutCheck(method, bean.getBean()));
            } else if (method.getAnnotations().length == 0 && clazz.isAnnotationPresent(Config.class)) {
                try {
                    method.invoke(bean.getBean());
                } catch (Exception e) {
                    // TODO : cannot run method <method name>.
                }
            }
        }
    }

    public void registerBean(List<Class<?>> scannedClasses)
            throws IllegalArgumentException, IllegalAccessException, BeanRegistryException {
        List<Class<?>> beanableClasses = getBeanableClasses(scannedClasses);
        List<Bean> beans = new ArrayList<>();
        for (Class<?> beanClass : beanableClasses) {
            Bean bean = createBeanWithoutCheck(beanClass);
            // @Dependency methods also return bean, thus a part of bean registry.
            runDependencyMethods(bean);
            beans.add(bean);
        }
        helper.autowire(beans);
    }
}
