package com.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.Date;
import java.util.HashMap;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import com.dao.UserDao;
import com.utils.Constants;
import com.utils.Utils;

public class LoginServlet extends HttpServlet {

  UserDao userDao = new UserDao();

  public HashMap<String, Object> getJsonResponse(String status, String message) {
    HashMap<String, Object> response = new HashMap<String, Object>();
    response.put(Constants.STATUS, status);
    response.put(Constants.MESSAGE, message);
    response.put(Constants.TIMESTAMP, new Date());
    return response;
  }

  public JSONObject login(HttpServletRequest request, Connection connection) throws IOException {
    HashMap<String, Object> map = Utils.extractElement(request);
    HashMap<String, Object> response = new HashMap<String, Object>();
    String username = (String) map.get("name");
    String password = (String) map.get("password");
    String dbUsername = "";
    try {
      dbUsername = userDao.getUsernameFromPassword(connection, password);
    } catch (Exception e) {
      response = getJsonResponse(Constants.FAILED, "No such user.");
    }
    if (username.equals(dbUsername)) {
      response = getJsonResponse(Constants.SUCCESS, "Logged In.");
    } else {
      response = getJsonResponse(Constants.FAILED, "Invalid Credentials.");
    }

    JSONObject jsonResponse = new JSONObject(response);
    return jsonResponse;
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

    Connection connection = null;
    try {
      connection = Utils.getConnection("postgres", "admin", "xyz@123", "library_management_system");
    } catch (Exception e) {
      e.printStackTrace();
    }

    response.setContentType("application/json");
    JSONObject jsonResponse = login(request, connection);
    PrintWriter out = response.getWriter();
    out.print(jsonResponse);
  }
}
