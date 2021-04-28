package com.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DispatcherServlet extends HttpServlet {

  private String requestUrl;

  public String extract(String url) {
    StringBuilder extractedUrl = new StringBuilder();
    for (int i = url.length() - 1; i > 0; i--) {
      if (url.charAt(i) == '/')
        break;
      extractedUrl.append(url.charAt(i));
    }
    return extractedUrl.toString();
  }

  @Override
  public void service(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    request.getRequestDispatcher("/online-library-system/login").include(request, response);
  }
}
