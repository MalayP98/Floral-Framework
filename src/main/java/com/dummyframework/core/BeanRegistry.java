package com.dummyframework.core;

import java.util.HashMap;
import java.util.Map;

import com.dummyframework.exception.BeanRegistryException;

public class BeanRegistry {

    private static BeanRegistry INSTANCE = null;

    private BeanRegistry() {
    }

    private Map<String, Bean> register = new HashMap<>();

    public static BeanRegistry getInstance() {
        if (INSTANCE == null) {
            return new BeanRegistry();
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

    public Object getBean(String beanName) {
        return register.get(beanName).getBean();
    }
}
