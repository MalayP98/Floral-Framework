package com.dummyframework.deserialize.converters;

import com.dummyframework.deserialize.TypeInfo;

public class StringConverter implements Converter {

    ConverterUtils utils = new ConverterUtils();

    @Override
    public Object convert(TypeInfo info, String content) throws ConverterException {
        return utils.removeQuotes(content);
    }
}