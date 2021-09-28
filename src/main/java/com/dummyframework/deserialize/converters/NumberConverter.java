package com.dummyframework.deserialize.converters;

import com.dummyframework.deserialize.TypeInfo;

public class NumberConverter implements Converter {

    ConverterUtils utils = new ConverterUtils();

    @Override
    public Object convert(TypeInfo info, String content) throws ConverterException {
        Class<?> clazz = info.getResolvedClass();
        if (clazz.isAssignableFrom(Integer.class)) {
            return Integer.valueOf(content);
        }
        if (clazz.isAssignableFrom(Long.class)) {
            return Long.valueOf(content);
        }
        if (clazz.isAssignableFrom(Float.class)) {
            return Float.valueOf(content);
        }
        if (clazz.isAssignableFrom(Double.class)) {
            return Double.valueOf(content);
        }
        return null;
    }
}
