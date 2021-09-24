package com.dummyframework.deserialize.converters;

import com.dummyframework.deserialize.TypeInfo;

public class BooleanConverter implements Converter {

    @Override
    public Object convert(TypeInfo info, String content) throws ConverterException {
        return Boolean.parseBoolean(content);
    }
}
