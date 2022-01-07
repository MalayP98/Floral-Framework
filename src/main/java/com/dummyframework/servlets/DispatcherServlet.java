package com.dummyframework.servlets;

import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.dummyframework.core.FrameworkSession;
import com.dummyframework.logger.Logger;

public class DispatcherServlet extends HttpServlet {

  Logger logger = new Logger(DispatcherServlet.class);

  @Override
  protected void service(HttpServletRequest request, HttpServletResponse response)
      throws IOException {

    FrameworkSession.setSession(request.getSession());
    // HandlerOperations ha = null;
    try {
      // ha = new HandlerOperations();
    } catch (Exception e1) {
      e1.printStackTrace();
    }
    try {
      // Object object = ha.invoke(request, response);
      // PrintWriter writer = response.getWriter();
      // writer.print(object);
    } catch (Exception e) {
      logger.error(e.getMessage());
      e.printStackTrace();
    }
  }
}
