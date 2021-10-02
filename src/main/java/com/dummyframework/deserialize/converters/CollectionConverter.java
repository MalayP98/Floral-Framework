package com.dummyframework.deserialize.converters;

import com.dummyframework.deserialize.TypeInfo;
import com.dummyframework.deserialize.builders.ArrayBuilder;
import com.dummyframework.deserialize.builders.ArrayBuilderException;
import com.dummyframework.deserialize.builders.CollectionBuilder;

public class CollectionConverter extends ArrayConverter {

    @Override
    public Object convert(TypeInfo info, String content) throws ConverterException, ArrayBuilderException {
        logger.info("Converting \"" + content + "\" to " + info.getResolvedClass().getSimpleName());
        ArrayBuilder builder = new CollectionBuilder();
        String[] parsedContent = parseContent(content);
        return builder.build(info, parsedContent);
    }
}
