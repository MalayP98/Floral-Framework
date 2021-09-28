package com.dummyframework.deserialize.builders;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import com.dummyframework.deserialize.TypeInfo;
import com.dummyframework.deserialize.converters.Converter;
import com.dummyframework.deserialize.converters.ConverterException;

public class CollectionBuilder implements ArrayBuilder {

    @Override
    public Object build(TypeInfo info, String[] content) throws ConverterException, ArrayBuilderException {
        Object object = createObject(info.getResolvedClass());
        TypeInfo genericTypeInfo = info.getGenerics()[0];
        return populate(info.getResolvedClass(), object, genericTypeInfo, content);

    }

    @SuppressWarnings("unchecked")
    private <T> Collection<T> populate(Class<T> clazz, Object object, TypeInfo genericTypeInfo, String[] content)
            throws ConverterException, ArrayBuilderException {
        Converter converter = genericTypeInfo.getConverter();
        Collection<T> coll = (Collection<T>) object;
        for (String element : content) {
            coll.add((T) converter.convert(genericTypeInfo, element));
        }
        return coll;
    }

    @SuppressWarnings("unchecked")
    private <T> Collection<T> createObject(Class<T> clazz) {
        if (clazz.isInterface()) {
            return createDefaultObject(clazz);
        }
        try {
            return (Collection<T>) clazz.getConstructor().newInstance();
        } catch (Exception e) {
            // TODO : no impl found.
        }
        return null;
    }

    private <T> Collection<T> createDefaultObject(Class<T> parent) {
        if (parent == List.class) {
            return new ArrayList<T>();
        }
        if (parent == Queue.class) {
            return new PriorityQueue<T>();
        }
        if (parent == Deque.class) {
            return new ArrayDeque<T>();
        }
        if (parent == Set.class) {
            return new HashSet<T>();
        }
        if (parent == SortedSet.class) {
            return new TreeSet<T>();
        }
        return null;
    }

}
