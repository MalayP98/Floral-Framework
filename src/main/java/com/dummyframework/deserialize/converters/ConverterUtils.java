package com.dummyframework.deserialize.converters;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConverterUtils {

    public final String QUOTES = "\"(.*)\"";

    public boolean isStringType(String content) {
        Pattern pattern = Pattern.compile(QUOTES);
        Matcher matcher = pattern.matcher(content);
        return matcher.find();
    }

    public String removeQuotes(String content) {
        Pattern pattern = Pattern.compile(QUOTES);
        Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return content;
    }
}
