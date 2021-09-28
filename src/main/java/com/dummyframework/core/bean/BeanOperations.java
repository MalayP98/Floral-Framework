package com.dummyframework.core.bean;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.dummyframework.annotations.Config;
import com.dummyframework.annotations.Dependency;
import com.dummyframework.core.AutowireHelper;
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
        Class<?> clazz = bean.getClazz();
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            // System.out.println("method name --> " + method.getName());
            // System.out.println("app count --> " + method.getAnnotations().length);
            // System.out.println("class ann --> " + clazz.getAnnotation(Config.class));
            if (method.isAnnotationPresent(Dependency.class)) {
                registry.addBean(createBeanWithoutCheck(method, bean.getBean()));
            } else if (method.getAnnotations().length == 0 && clazz.isAnnotationPresent(Config.class)) {
                try {
                    System.out.println(bean.getBean());
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
            registry.addBean(bean);
            // @Dependency methods also return bean, thus a part of bean registry.
            runDependencyMethods(bean);
            beans.add(bean);
        }
        helper.autowire(beans);
    }
}
