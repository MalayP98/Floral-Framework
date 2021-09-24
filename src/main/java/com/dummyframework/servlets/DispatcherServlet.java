package com.dummyframework.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Objects;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.dummyframework.core.DummyFramework;
import com.dummyframework.core.FrameworkSession;
import com.dummyframework.core.HandleResponse;
import com.dummyframework.core.HandlerAdapter;
import com.dummyframework.core.HandlerOperations;
import com.dummyframework.core.WebApplicationContext;
import com.dummyframework.exception.AppContextException;
import com.dummyframework.utils.Constants;

public class DispatcherServlet extends HttpServlet {

  private WebApplicationContext webApplicationContext;

  public DispatcherServlet() throws AppContextException {
    webApplicationContext = DummyFramework.getWebApplicationContext();
  }

  @Override
  protected void service(HttpServletRequest request, HttpServletResponse response) throws IOException {

    FrameworkSession.setSession(request.getSession());
    HandlerOperations ha = null;
    try {
      ha = new HandlerOperations(webApplicationContext);
    } catch (Exception e1) {
      e1.printStackTrace();
    }
    try {
      Object object = ha.invoke(request, response);
      PrintWriter writer = response.getWriter();
      writer.print(object);
    } catch (Exception e) {
      System.out.println(e.getMessage());
      System.out.println(e.getStackTrace());
    }
  }
}
