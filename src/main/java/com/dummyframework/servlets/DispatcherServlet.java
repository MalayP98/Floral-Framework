package com.dummyframework.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DispatcherServlet extends HttpServlet {
  @Override
  public void service(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {

    request.getRequestDispatcher("/librarysystem.com/login").forward(request, response);

  }
}
