package com.dummyframework.deserialize.builders;

import com.dummyframework.deserialize.TypeInfo;
import com.dummyframework.deserialize.converters.ConverterException;

public class ResolveBuilder {

    public static ArrayBuilder selectBuilder(TypeInfo info) throws ConverterException, ArrayBuilderException {
        if (info.isPrimitive()) {
            return selectPrimitiveArrayBuilder(info);
        } else {
            return new GenericArrayBuilder();
        }
    }

    public static ArrayBuilder selectPrimitiveArrayBuilder(TypeInfo info) throws ArrayBuilderException {
        if (info.isAssignableFrom(int.class)) {
            return new PrimitiveArrayBuilders.PrimitiveIntArrayBuilder();
        }
        if (info.isAssignableFrom(long.class)) {
            return new PrimitiveArrayBuilders.PrimitiveLongArrayBuilder();
        }
        if (info.isAssignableFrom(double.class)) {
            return new PrimitiveArrayBuilders.PrimitiveDoubleArrayBuilder();
        }
        if (info.isAssignableFrom(float.class)) {
            return new PrimitiveArrayBuilders.PrimitiveFloatArrayBuilder();
        }
        if (info.isAssignableFrom(boolean.class)) {
            return new PrimitiveArrayBuilders.PrimitiveBooleanArrayBuilder();
        }
        if (info.isAssignableFrom(char.class)) {
            return new PrimitiveArrayBuilders.PrimitiveCharArrayBuilder();
        }
        throw new ArrayBuilderException("No array builder found.");
    }
}
