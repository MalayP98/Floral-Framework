package com.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.JSONObject;
import com.dao.BooksDao;
import com.dao.UserDao;
import com.utils.Constants;
import com.utils.Utils;

public class IssueServlet extends HttpServlet {

  UserDao userDao = new UserDao();
  BooksDao bookDao = new BooksDao();

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    HttpSession session;
    PrintWriter out = response.getWriter();
    session = request.getSession();
    response.setContentType("application/json");

    JSONObject jsonResponse = null;
    try {
      jsonResponse = issueBook(request, session);
    } catch (Exception e) {
      System.out.println(e.getMessage());
      jsonResponse = new JSONObject(Utils.getJsonResponse(Constants.FAILED, "Try Again."));
    }
    out.print(jsonResponse);
  }

  private JSONObject issueBook(HttpServletRequest request, HttpSession session)
      throws IOException, SQLException {
    boolean canIssue = (session != null);
    HashMap<String, Object> response;
    if (canIssue) {
      HashMap<String, Object> map = Utils.extractData(request);
      String[] books = (String[]) map.get("books");
      try {
        bookDao.issuseBooks(books, Integer.parseInt((String) session.getAttribute("id")));
        response = Utils.getJsonResponse(Constants.SUCCESS, "Books issued.");
      } catch (Exception e) {
        System.out.println(e.getMessage());
        response = Utils.getJsonResponse(Constants.FAILED, "Something wrong. Try again.");
      }
    } else {
      response = Utils.getJsonResponse(Constants.FAILED, "Authorization Failed.");
    }
    return new JSONObject(response);
  }
}
