package com.dummyframework.core;

import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import com.dummyframework.exception.NoPatternMatchedException;
import com.dummyframework.utils.Constants;
import com.dummyframework.utils.FrameworkUtils;

public class HandlerMapper {

  public HandlerDetails findMethodForUrl(HashMap<String, HandlerDetails> urlMap, HttpServletRequest request)
      throws NoPatternMatchedException {
    String url = FrameworkUtils.matchPattern(request.getRequestURI(), Constants.REQUEST_REGEX, 3);
    String requestType = request.getMethod();
    HandlerDetails details = urlMap.get(url + "#" + requestType);
    return details;
  }

}
