package com.dummyframework.core.bean;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.dummyframework.annotations.Config;
import com.dummyframework.annotations.Dependency;
import com.dummyframework.core.AutowireHelper;
import com.dummyframework.exception.BeanRegistryException;
import com.dummyframework.logger.Logger;

public class BeanOperations extends BeanGenerator {

    Logger logger = new Logger(BeanGenerator.class);

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
            if (method.isAnnotationPresent(Dependency.class)) {
                logger.info("Creating bean with name \"" + method.getName() + "\"");
                registry.addBean(createBeanWithoutCheck(method, bean.getBean()));
            } else if (method.getAnnotations().length == 0 && clazz.isAnnotationPresent(Config.class)) {
                try {
                    method.invoke(bean.getBean());
                } catch (Exception e) {
                    logger.error("Cannot create bean with name \"" + method.getName() + "\"");
                }
            }
        }
    }

    public void registerBean(List<Class<?>> scannedClasses)
            throws IllegalArgumentException, IllegalAccessException, BeanRegistryException {
        logger.info("Bean registration start.");
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
