package com.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.HashMap;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.JSONObject;
import com.dao.UserDao;
import com.utils.Constants;
import com.utils.Utils;

public class LoginServlet extends HttpServlet {

  UserDao userDao = new UserDao();

  public JSONObject login(HttpServletRequest request, HttpSession session)
      throws IOException, SQLException {
    HashMap<String, Object> map = Utils.extractData(request);
    HashMap<String, Object> response = new HashMap<String, Object>();
    String username = (String) map.get("name");
    String password = (String) map.get("password");
    String dbUsername = "";
    try {
      dbUsername = userDao.getUsernameFromPassword(password);
    } catch (Exception e) {
      response = Utils.getJsonResponse(Constants.FAILED, "No such user.");
    }
    if (username.equals(dbUsername)) {
      response = Utils.getJsonResponse(Constants.SUCCESS, "Logged In.");
      String role = userDao.getRoleFromUsernameAndPassword(username, password);
      session.setAttribute("username", username);
      session.setAttribute("role", role);
    } else {
      response = Utils.getJsonResponse(Constants.FAILED, "Invalid Credentials.");
    }

    JSONObject jsonResponse = new JSONObject(response);
    return jsonResponse;
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    HttpSession session = request.getSession();

    response.setContentType("application/json");
    JSONObject jsonResponse = null;
    try {
      jsonResponse = login(request, session);
    } catch (Exception e) {
      e.getMessage();
    }
    PrintWriter out = response.getWriter();
    out.print(jsonResponse);
  }
}
