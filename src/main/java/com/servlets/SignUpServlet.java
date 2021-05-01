package com.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.Date;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import com.dao.UserDao;
import com.models.ROLES;
import com.models.User;
import com.utils.Constants;
import com.utils.Utils;

public class SignUpServlet extends HttpServlet {

  UserDao userDao = new UserDao();

  public HashMap<String, Object> getJsonResponse(String status, String message) {
    HashMap<String, Object> response = new HashMap<String, Object>();
    response.put(Constants.STATUS, status);
    response.put(Constants.MESSAGE, message);
    response.put(Constants.TIMESTAMP, new Date());
    return response;
  }

  public User populateUser(HashMap<String, Object> map) {
    User user = new User();
    user.setUsername((String) map.get("username"));
    user.setPassword((String) map.get("password"));
    user.setEmail((String) map.get("email"));
    user.setRole((map.get("role").equals("admin")) ? ROLES.admin : ROLES.user);
    user.setIssuedBooks(0);
    return user;
  }

  private JSONObject addUser(HttpServletRequest request, Connection connection) throws IOException {
    HashMap<String, Object> map = Utils.extractData(request);
    HashMap<String, Object> response = new HashMap<String, Object>();
    User user = populateUser(map);
    try {
      userDao.addUser(connection, user);
      response = getJsonResponse(Constants.SUCCESS, "Signup Successfull");
    } catch (Exception e) {
      System.out.println(e.getMessage());
      response = getJsonResponse(Constants.FAILED, "Something wrong. Enter details again.");
    }

    JSONObject jsonResponse = new JSONObject(response);
    return jsonResponse;
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    Connection connection = null;

    try {
      connection = Utils.getConnection("postgres", "admin", "xyz@123", "library_management_system");
    } catch (Exception e) {
      e.printStackTrace();
    }

    response.setContentType("application/json");
    JSONObject jsonResponse = addUser(request, connection);
    PrintWriter out = response.getWriter();
    out.print(jsonResponse);
  }

}
