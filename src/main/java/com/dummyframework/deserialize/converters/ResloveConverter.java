package com.dummyframework.deserialize.converters;

import com.dummyframework.deserialize.TypeInfo;

public class ResloveConverter {

    public static Converter getConverter(TypeInfo info) throws ConverterException {
        if (info.isArray()) {
            return new ArrayConverter();
        }
        if (info.isPrimitive()) {
            return new PrimitiveConverter();
        }
        if (info.isAssignableFrom(Number.class)) {
            return new NumberConverter();
        }
        if (info.isAssignableFrom(String.class)) {
            return new StringConverter();
        }
        if (info.isAssignableFrom(Boolean.class)) {
            return new BooleanConverter();
        }
        if (info.isAssignableFrom(Character.class)) {
            return new CharacterConverter();
        }
        if (info.isMap()) {
            return null;
            // TODO: return map converter.
        }
        if (info.isCollection()) {
            return new CollectionConverter();
            // TODO: return list converter.
        }
        if (info.isEnum()) {
            return null;
            // TODO: return enum converte.
        }
        if (!info.isJavaType()) {
            return new SimpleObjectConverter();
        }
        throw new ConverterException("No converter found.");
    }
}
