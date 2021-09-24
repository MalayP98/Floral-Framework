package com.dummyframework.deserialize.builders;

import java.lang.reflect.Array;

import com.dummyframework.deserialize.TypeInfo;
import com.dummyframework.deserialize.converters.Converter;
import com.dummyframework.deserialize.converters.ConverterException;

public class GenericArrayBuilder implements ArrayBuilder {

    @Override
    public Object build(TypeInfo info, String[] content) throws ConverterException, ArrayBuilderException {
        return build(info.getArrayComponentTypeInfo(), info.getResolvedClass(), content);
    }

    @SuppressWarnings("unchecked")
    private <T> T[] build(TypeInfo info, Class<T> clazz, String[] rawArray)
            throws ConverterException, ArrayBuilderException {
        T[] array = (T[]) Array.newInstance(clazz, rawArray.length);
        Object finalArray = populate(info, rawArray, array);
        return (T[]) finalArray;
    }

    private Object populate(TypeInfo info, String[] rawArray, Object[] actualArray)
            throws ConverterException, ArrayBuilderException {
        Converter converter = info.getConverter();
        for (int i = 0; i < rawArray.length; i++) {
            actualArray[i] = converter.convert(info, rawArray[i]);
        }
        return actualArray;
    }
}
