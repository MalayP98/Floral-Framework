package com.dummyframework.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dummyframework.core.FrameworkSession;
import com.dummyframework.core.handler.HandlerOperations;

public class DispatcherServlet extends HttpServlet {

  @Override
  protected void service(HttpServletRequest request, HttpServletResponse response) throws IOException {

    FrameworkSession.setSession(request.getSession());
    HandlerOperations ha = null;
    try {
      ha = new HandlerOperations();
    } catch (Exception e1) {
      System.out.println(e1.getStackTrace());
    }
    try {
      Object object = ha.invoke(request, response);
      PrintWriter writer = response.getWriter();
      writer.print(object);
    } catch (Exception e) {
      System.out.println(e.getMessage());
      e.printStackTrace();
    }
  }
}
