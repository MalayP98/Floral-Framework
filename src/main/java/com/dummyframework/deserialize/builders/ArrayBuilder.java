package com.dummyframework.deserialize.builders;

import com.dummyframework.deserialize.TypeInfo;
import com.dummyframework.deserialize.converters.ConverterException;

public interface ArrayBuilder {

    public Object build(TypeInfo info, String[] content) throws ConverterException, ArrayBuilderException;

}
