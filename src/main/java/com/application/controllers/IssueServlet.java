package com.application.controllers;

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
import com.application.dao.BooksDao;
import com.application.dao.UserDao;
import com.application.utils.Constants;
import com.application.utils.Utils;

public class IssueServlet extends HttpServlet {

  UserDao userDao = new UserDao();
  BooksDao bookDao = new BooksDao();

  public String handleRequest(HttpServletRequest request) {
    String uri = request.getRequestURI();
    String[] seprate = uri.split("/");
    return seprate[seprate.length - 1];
  }

  public JSONObject callRequiredFunction(HttpServletRequest request, HttpSession session)
      throws IOException, SQLException {
    String uri = handleRequest(request);
    switch (uri) {
      case "issueBook":
        return issueBook(request, session);
      case "returnBook":
        return returnBook(request, session);
      default:
        return null;
    }
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    HttpSession session;
    PrintWriter out = response.getWriter();
    session = request.getSession();
    response.setContentType("application/json");


    JSONObject jsonResponse = null;
    try {
      jsonResponse = callRequiredFunction(request, session);
    } catch (Exception e) {
      System.out.println(e.getMessage());
      jsonResponse = new JSONObject(Utils.getMapResponse(Constants.FAILED, "Try Again."));
    }
    out.print(jsonResponse);
  }

  private JSONObject issueBook(HttpServletRequest request, HttpSession session)
      throws IOException, SQLException {
    boolean canIssue = (session != null);
    HashMap<String, Object> response;
    if (canIssue) {
      HashMap<String, Object> map = Utils.extractData(request);
      String[] books;
      Object object = map.get("books");
      String className = object.getClass().getName();
      if (className.equals("java.lang.String")) {
        books = new String[1];
        books[0] = (String) object;
      } else
        books = (String[]) object;
      try {
        bookDao.issuseBooks(books, Integer.parseInt((String) session.getAttribute("id")));
        response = Utils.getMapResponse(Constants.SUCCESS, "Books issued.");
      } catch (Exception e) {
        System.out.println(e.getMessage());
        response = Utils.getMapResponse(Constants.FAILED, "Something wrong. Try again.");
      }
    } else {
      response = Utils.getMapResponse(Constants.FAILED, "Authorization Failed.");
    }
    return new JSONObject(response);
  }

  private JSONObject returnBook(HttpServletRequest request, HttpSession session)
      throws IOException {
    boolean canReturn = (session != null);
    HashMap<String, Object> response;
    if (canReturn) {
      HashMap<String, Object> map = Utils.extractData(request);
      String[] books;
      Object object = map.get("books");
      String className = object.getClass().getName();
      if (className.equals("java.lang.String")) {
        books = new String[1];
        books[0] = (String) object;
      } else
        books = (String[]) object;
      try {
        bookDao.returnBook(books, Integer.parseInt((String) session.getAttribute("id")));
        response = Utils.getMapResponse(Constants.SUCCESS, "Books returned.");
      } catch (Exception e) {
        System.out.println(e.getMessage());
        response = Utils.getMapResponse(Constants.FAILED, "Something wrong. Try again.");
      }
    } else {
      response = Utils.getMapResponse(Constants.FAILED, "Login to return the books.");
    }
    return new JSONObject(response);
  }
}
