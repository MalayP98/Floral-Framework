package com.servlets;

import java.io.IOException;
import java.sql.Connection;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.dao.UserDao;
import com.utils.Utils;

public class LoginServlet extends HttpServlet {

  UserDao userDao = new UserDao();

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

    Connection connection = null;
    try {
      connection = Utils.getConnection("postgres", "admin", "xyz@123", "library_management_system");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
