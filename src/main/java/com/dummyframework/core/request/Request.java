package com.dummyframework.core.request;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import com.dummyframework.core.DefaultProperties;
import com.dummyframework.core.Properties;
import com.dummyframework.utils.RequestMethod;

public class Request {

  private String url;
  private HashMap<String, String> queryParams = new HashMap<>();
  private String payload;
  private RequestMethod method;

  public Request(HttpServletRequest request) throws Exception {
    resolveURL(request.getRequestURI());
    this.payload = readPayload(request.getReader());
    this.method = RequestMethod.toRequestMethod(request.getMethod());
    resolveQueryParams(request.getQueryString());
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
    url = removeAppName(url);
  }

  private String removeAppName(String url) {
    return url.replace("/" + Properties.get(DefaultProperties.APP_NAME), "");
  }

  private void resolveQueryParams(String params) {
    if (Objects.nonNull(params) && !params.isEmpty()) {
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

  public RequestMethod getMethod() {
    return this.method;
  }
}
