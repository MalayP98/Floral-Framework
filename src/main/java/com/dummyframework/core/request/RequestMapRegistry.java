package com.dummyframework.core.request;

import java.util.HashMap;
import java.util.Map;

import com.dummyframework.core.handler.HandlerDetails;
import com.dummyframework.utils.RequestMethod;

public class RequestMapRegistry {

    private static RequestMapRegistry INSTANCE = null;

    private RequestMapRegistry() {
    }

    public static RequestMapRegistry getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RequestMapRegistry();
        }
        return INSTANCE;
    }

    private static Map<String, HandlerDetails> urlRegistry = new HashMap<>();

    public void add(String url, RequestMethod method, HandlerDetails details) {
        urlRegistry.put(generateKey(url, method.toString()), details);
    }

    public void add(String url, String method, HandlerDetails details) {
        urlRegistry.put(generateKey(url, method), details);
    }

    public HandlerDetails get(String url, String method) {
        String key = generateKey(url, method);
        // throw error here.
        if (!urlRegistry.containsKey(key)) {
            return null;
        }
        return urlRegistry.get(key);
    }

    public HandlerDetails get(Request request) {
        return get(request.getUrl(), request.getMethod());
    }

    private String generateKey(String url, String method) {
        return url + "#" + method;
    }
}
