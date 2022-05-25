package com.dummyframework.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.dummyframework.core.FrameworkSession;
import com.dummyframework.core.handler.HandlerProvider;
import com.dummyframework.core.request.Request;
import com.dummyframework.logger.Logger;

public class DispatcherServlet extends HttpServlet {

  Logger logger = new Logger(DispatcherServlet.class);

  @Override
  protected void service(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    FrameworkSession.setSession(request.getSession());
    HandlerProvider provider = new HandlerProvider();
    try {
      Object output = provider.invokeHandler(new Request(request));
      PrintWriter writer = response.getWriter();
      writer.print(output);
    } catch (Exception e) {
      logger.error(e.getMessage());
      e.printStackTrace();
    }
  }
}
