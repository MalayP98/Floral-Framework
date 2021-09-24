package com.dummyframework.deserialize.converters;

import com.dummyframework.deserialize.TypeInfo;

public class CharacterConverter implements Converter {

    ConverterUtils utils = new ConverterUtils();

    @Override
    public Object convert(TypeInfo info, String content) throws ConverterException {
        return Character.valueOf(stringToChar(content));
    }

    public char stringToChar(String content) throws ConverterException {
        if (!utils.isStringType(content)) {
            throw new ConverterException("Not a String input.");
        }
        content = utils.removeQuotes(content);
        if (content.length() > 1) {
            throw new ConverterException("Connot convert string to char");
        }
        return content.charAt(0);
    }

}
