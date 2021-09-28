package com.dummyframework.core;

import java.util.HashMap;
import java.util.Map;

public class Properties {

    private static Map<String, String> properties = new HashMap<>();

    public static void set(String property, String value) {
        properties.put(property, value);
    }

    public static String get(String property) {
        if (!properties.containsKey(property)) {
            return null;
        }
        return properties.get(property);
    }

}
