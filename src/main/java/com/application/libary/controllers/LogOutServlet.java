package com.application.libary.controllers;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.JSONObject;
import com.application.libary.utils.Constants;
import com.application.libary.utils.Utils;
import com.dummyframework.annotations.Controller;

@Controller
public class LogOutServlet extends HttpServlet {

  @Override
  protected void service(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    HttpSession session = request.getSession();
    response.setContentType("application/json");
    session.invalidate();
    JSONObject jsonResponse =
        new JSONObject(Utils.getMapResponse(Constants.SUCCESS, "Logged Out."));
    out.print(jsonResponse);
  }

}
