package com.dummyframework.deserialize.converters;

import com.dummyframework.deserialize.TypeInfo;

public class PrimitiveConverter implements Converter {

    ConverterUtils utils = new ConverterUtils();

    @Override
    public Object convert(TypeInfo info, String content) throws ConverterException {
        Class<?> clazz = info.getResolvedClass();
        if (clazz.isAssignableFrom(int.class)) {
            return Integer.parseInt(content);
        }
        if (clazz.isAssignableFrom(long.class)) {
            return Long.parseLong(content);
        }
        if (clazz.isAssignableFrom(float.class)) {
            return Float.parseFloat(content);
        }
        if (clazz.isAssignableFrom(double.class)) {
            return Double.parseDouble(content);
        }
        if (clazz.isAssignableFrom(boolean.class)) {
            return Boolean.parseBoolean(content);
        }
        if (clazz.isAssignableFrom(char.class)) {
            CharacterConverter converter = new CharacterConverter();
            return converter.stringToChar(content);
        }
        return null;
    }

}
