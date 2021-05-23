package com.dummyframework.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.dummyframework.core.DummyFramework;
import com.dummyframework.core.FrameworkSession;
import com.dummyframework.core.HandleResponse;
import com.dummyframework.core.HandlerAdapter;
import com.dummyframework.core.WebApplicationContext;
import com.dummyframework.exception.AppContextException;

public class DispatcherServlet extends HttpServlet {

  private WebApplicationContext webApplicationContext;

  public DispatcherServlet() throws AppContextException {
    webApplicationContext = DummyFramework.getWebApplicationContext();
  }

  @Override
  protected void service(HttpServletRequest request, HttpServletResponse response)
      throws IOException {

    FrameworkSession.setSession(request.getSession());
    HandlerAdapter ha = new HandlerAdapter(webApplicationContext);
    try {
      Object object = ha.invokeMethod(request, response);
      if (object != null) {
        Object responseObject = HandleResponse.handle(object, response.getContentType());
        PrintWriter writer = response.getWriter();
        writer.print(responseObject);
      }
    } catch (Exception e) {
      System.out.println("---------------" + e.getMessage() + "-----------");
      System.out.println(e.getStackTrace());
    }
  }
}


