package com.dummyframework.deserialize.converters;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.dummyframework.deserialize.Separator;
import com.dummyframework.deserialize.TypeInfo;
import com.dummyframework.deserialize.builders.ArrayBuilderException;
import com.dummyframework.deserialize.builders.MapBuilder;

public class MapConverter implements Converter {

    private Separator separator = new Separator();
    private final String PARENTHESIS = "\\((.*)\\)";

    private String removeParenthesis(String content) throws ConverterException {
        Pattern pattern = Pattern.compile(PARENTHESIS);
        Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {
            return matcher.group(1);
        }
        throw new ConverterException("Not a valid input.");
    }

    @Override
    public Object convert(TypeInfo info, String content) throws ConverterException, ArrayBuilderException {
        MapBuilder builder = new MapBuilder();
        Map<String, String> keyValue = separator.toMap(removeParenthesis(content));
        return builder.build(info, keyValue);
    }
}
