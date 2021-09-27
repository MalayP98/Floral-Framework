package com.dummyframework.core.bean;

public class Bean {

    private Object bean;
    private Class<?> clazz;
    private String beanName;

    public Bean(BeanBuilder builder) {
        this.bean = builder.getBean();
        this.clazz = builder.getClazz();
        this.beanName = builder.getBeanName();
    }

    public Object getBean() {
        return bean;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public String getBeanName() {
        return beanName;
    }
}
