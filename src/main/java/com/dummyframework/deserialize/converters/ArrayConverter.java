package com.dummyframework.deserialize.converters;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.dummyframework.deserialize.Separator;
import com.dummyframework.deserialize.TypeInfo;
import com.dummyframework.deserialize.builders.ArrayBuilder;
import com.dummyframework.deserialize.builders.ArrayBuilderException;
import com.dummyframework.deserialize.builders.ResolveBuilder;

public class ArrayConverter implements Converter {
    private final String SQUARE_BRACKET = "\\[(.*)\\]";
    Separator separator = new Separator();

    @Override
    public Object convert(TypeInfo info, String content) throws ConverterException, ArrayBuilderException {
        ArrayBuilder builder = ResolveBuilder.selectBuilder(info);
        return builder.build(info, parseContent(content));
    }

    protected String[] parseContent(String content) throws ConverterException {
        content = removeSquareBrackets(content);
        String[] separated = separator.toArray(content);
        return separated;
    }

    private String removeSquareBrackets(String content) throws ConverterException {
        Pattern pattern = Pattern.compile(SQUARE_BRACKET);
        Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {
            return matcher.group(1);
        }
        throw new ConverterException("Supplied content is not Array type.");
    }

}
