package com.dummyframework.core.request;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;

public class Request {

    private String url;
    private HashMap<String, String> queryParams;
    private String payload;
    private String method;

    public Request(HttpServletRequest request) throws IOException {
        resolveURL(request.getRequestURI());
        this.payload = readPayload(request.getReader());
        this.method = request.getMethod();
    }

    private String readPayload(BufferedReader reader) throws IOException {
        String payload = reader.readLine();
        if (payload.isEmpty())
            return null;
        return payload;
    }

    // need fix, no '?' present in uri
    private void resolveURL(String uri) {
        this.url = "";
        char lastChar = uri.charAt(uri.length() - 1);
        if (lastChar == '/') {
            this.url = uri.substring(0, uri.length() - 1);
        } else {
            this.url = uri;
        }
    }

    private void resolveQueryParams(String params) {
        if (!params.isEmpty()) {
            String[] paramsArr = params.split("&");
            for (String param : paramsArr) {
                int i;
                for (i = 0; i < param.length(); i++) {
                    if (param.charAt(i) == '=') {
                        break;
                    }
                }
                this.queryParams.put(param.substring(0, i), param.substring(i + 1));
            }
        } else {
            this.payload = "";
        }
    }

    public boolean hasPayload() {
        return this.payload.isEmpty();
    }

    public boolean hasQueryParams() {
        return (this.queryParams.isEmpty());
    }

    public String getUrl() {
        return url;
    }

    public HashMap<String, String> getQueryParams() {
        return queryParams;
    }

    public String getPayload() {
        return payload;
    }

    public String getMethod() {
        return this.method;
    }
}
