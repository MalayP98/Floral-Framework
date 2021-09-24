package com.dummyframework.deserialize.builders;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import com.dummyframework.deserialize.TypeInfo;
import com.dummyframework.deserialize.converters.Converter;
import com.dummyframework.deserialize.converters.ConverterException;

public class MapBuilder {

    public Object build(TypeInfo info, Map<String, String> map) {
        Object object = createObject(info.getResolvedClass());
        try {
            TypeInfo[] generics = info.getGenerics();
            Class<?> keyClass = generics[0].getResolvedClass();
            Class<?> valueClass = generics[1].getResolvedClass();
            populate(info, map, object, keyClass, valueClass);
        } catch (Exception e) {

        }
        return object;
    }

    @SuppressWarnings("unchecked")
    private <K, V> void populate(TypeInfo info, Map<String, String> strMap, Object object, Class<K> keyClass,
            Class<V> valueClass) throws ConverterException, ArrayBuilderException {
        TypeInfo keyTypeInfo = info.getGenerics()[0];
        TypeInfo valueTypeInfo = info.getGenerics()[1];
        Converter keyConverter = keyTypeInfo.getConverter();
        Converter valueConverter = valueTypeInfo.getConverter();
        Map<K, V> map = (Map<K, V>) object;
        for (Map.Entry<String, String> entrySet : strMap.entrySet()) {
            map.put((K) keyConverter.convert(keyTypeInfo, (String) entrySet.getKey()),
                    (V) valueConverter.convert(valueTypeInfo, (String) entrySet.getValue()));
        }
    }

    private Object createObject(Class<?> clazz) {
        if (clazz.isInterface()) {
            return createDefaultObject(clazz);
        }
        try {
            return clazz.getConstructor().newInstance();
        } catch (Exception e) {
            // TODO :
        }
        return null;
    }

    private Object createDefaultObject(Class<?> clazz) {
        if (clazz == Map.class) {
            return new HashMap<>();
        }
        if (clazz == SortedMap.class) {
            return new TreeMap<>();
        }
        return null;
    }

}
