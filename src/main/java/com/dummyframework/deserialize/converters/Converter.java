package com.dummyframework.deserialize.converters;

import com.dummyframework.deserialize.TypeInfo;
import com.dummyframework.deserialize.builders.ArrayBuilderException;

public interface Converter {

    public Object convert(TypeInfo info, String content) throws ConverterException, ArrayBuilderException;

}
