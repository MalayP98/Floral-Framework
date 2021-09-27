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
            return new RequestMapRegistry();
        }
        return INSTANCE;
    }

    private static Map<String, HandlerDetails> urlRegistry = new HashMap<>();

    public void add(String url, RequestMethod method, HandlerDetails details) {
        System.out.println("mapping " + generateKey(url, method.toString()) + " --> " + details.toString());
        urlRegistry.put(generateKey(url, method.toString()), details);
        System.out.println(urlRegistry.size() + " \n");
    }

    public void add(String url, String method, HandlerDetails details) {
        urlRegistry.put(generateKey(url, method), details);
    }

    public HandlerDetails get(String url, String method) {

        System.out.println("***** " + urlRegistry.size());

        for (Map.Entry<String, HandlerDetails> set : urlRegistry.entrySet()) {
            System.out.println(set.getKey() + " --> " + set.getValue());
        }

        String key = generateKey(url, method);
        System.out.println("getting --> " + key + " --> is present " + urlRegistry.containsKey(key));
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
