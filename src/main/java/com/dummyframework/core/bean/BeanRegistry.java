package com.dummyframework.core.bean;

import java.util.HashMap;
import java.util.Map;

import com.dummyframework.exception.BeanRegistryException;

public class BeanRegistry {

    private static BeanRegistry INSTANCE = null;

    private BeanRegistry() {
    }

    private static Map<String, Bean> register = new HashMap<>();

    public static BeanRegistry getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new BeanRegistry();
        }
        return INSTANCE;
    }

    public void addBean(Bean bean) throws BeanRegistryException {
        if (!register.containsKey(bean.getBeanName())) {
            register.put(bean.getBeanName(), bean);
        } else {
            throw new BeanRegistryException(
                    "Cannot add bean " + bean.getBeanName() + ", as a bean with same name is already present.");
        }
    }

    public Bean getSimpleBean(String beanName) {
        return register.get(beanName);
    }

    public Object getBean(String beanName) {
        return getSimpleBean(beanName).getBean();
    }
}
