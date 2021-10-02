package com.dummyframework.deserialize.converters;

import com.dummyframework.deserialize.TypeInfo;

public class BooleanConverter implements Converter {

    @Override
    public Object convert(TypeInfo info, String content) throws ConverterException {
        logger.info("Converting \"" + content + "\" Boolean");
        return Boolean.parseBoolean(content);
    }
}
