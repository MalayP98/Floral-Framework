package com.dummyframework.deserialize.builders;

import com.dummyframework.deserialize.TypeInfo;
import com.dummyframework.deserialize.converters.Converter;
import com.dummyframework.deserialize.converters.ConverterException;
import com.dummyframework.deserialize.converters.PrimitiveConverter;

public class PrimitiveArrayBuilders {

    public static class PrimitiveIntArrayBuilder implements ArrayBuilder {

        @Override
        public Object build(TypeInfo info, String[] content) throws ConverterException, ArrayBuilderException {
            Converter converter = new PrimitiveConverter();
            int[] array = new int[content.length];
            for (int i = 0; i < content.length; i++) {
                array[i] = (int) converter.convert(info, content[i]);
            }
            return array;
        }

    }

    public static class PrimitiveLongArrayBuilder implements ArrayBuilder {

        @Override
        public Object build(TypeInfo info, String[] content) throws ConverterException, ArrayBuilderException {
            Converter converter = new PrimitiveConverter();
            long[] array = new long[content.length];
            for (int i = 0; i < content.length; i++) {
                array[i] = (long) converter.convert(info, content[i]);
            }
            return array;
        }
    }

    public static class PrimitiveFloatArrayBuilder implements ArrayBuilder {

        @Override
        public Object build(TypeInfo info, String[] content) throws ConverterException, ArrayBuilderException {
            Converter converter = new PrimitiveConverter();
            float[] array = new float[content.length];
            for (int i = 0; i < content.length; i++) {
                array[i] = (float) converter.convert(info, content[i]);
            }
            return array;
        }
    }

    public static class PrimitiveDoubleArrayBuilder implements ArrayBuilder {

        @Override
        public Object build(TypeInfo info, String[] content) throws ConverterException, ArrayBuilderException {
            Converter converter = new PrimitiveConverter();
            double[] array = new double[content.length];
            for (int i = 0; i < content.length; i++) {
                array[i] = (double) converter.convert(info, content[i]);
            }
            return array;
        }
    }

    public static class PrimitiveBooleanArrayBuilder implements ArrayBuilder {

        @Override
        public Object build(TypeInfo info, String[] content) throws ConverterException, ArrayBuilderException {
            Converter converter = new PrimitiveConverter();
            boolean[] array = new boolean[content.length];
            for (int i = 0; i < content.length; i++) {
                array[i] = (boolean) converter.convert(info, content[i]);
            }
            return array;
        }
    }

    public static class PrimitiveCharArrayBuilder implements ArrayBuilder {

        @Override
        public Object build(TypeInfo info, String[] content) throws ConverterException, ArrayBuilderException {
            Converter converter = new PrimitiveConverter();
            char[] array = new char[content.length];
            for (int i = 0; i < content.length; i++) {
                array[i] = (char) converter.convert(info, content[i]);
            }
            return array;
        }
    }

}
