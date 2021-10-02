package com.dummyframework.deserialize.converters;

import com.dummyframework.deserialize.TypeInfo;
import com.dummyframework.deserialize.builders.ArrayBuilderException;
import com.dummyframework.logger.Logger;

public interface Converter {

    public Logger logger = new Logger(Converter.class);

    public Object convert(TypeInfo info, String content) throws ConverterException, ArrayBuilderException;

}
