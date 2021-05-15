package com.application.libary.controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.JSONObject;
import com.application.libary.dao.BooksDao;
import com.application.libary.models.Book;
import com.application.libary.utils.Constants;
import com.application.libary.utils.Utils;

public class BookServlet extends HttpServlet {

  BooksDao booksDao = new BooksDao();

  public boolean verify(HttpSession session) {
    return (session.getAttribute("role").equals("admin")) ? true : false;
  }

  public JSONObject addBook(HttpServletRequest request, HttpSession session) throws IOException {
    HashMap<String, Object> response = null;
    boolean canAdd = verify(session);
    if (canAdd) {
      try {
        HashMap<String, Object> map = Utils.extractData(request);
        Book book = populateBook(map);
        booksDao.addBook(book);
        response = Utils.getMapResponse(Constants.SUCCESS, "Book added successfully.");
      } catch (Exception e) {
        System.out.println(e.getMessage());
        response = Utils.getMapResponse(Constants.FAILED, "Something wrong. Enter data again.");
      }
    } else {
      response = Utils.getMapResponse(Constants.FAILED, "Authorization Failed.");
    }
    return new JSONObject(response);
  }

  private Book populateBook(HashMap<String, Object> map) {
    Book book = new Book();
    book.setBookName((String) map.get("name"));
    book.setAuthor((String) map.get("author"));
    book.setCopies(Integer.parseInt(map.get("copies").toString()));
    book.setIsbn((String) map.get("ISBN"));
    return book;
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    HttpSession session;
    PrintWriter out = response.getWriter();

    session = request.getSession();
    response.setContentType("application/json");
    JSONObject jsonResponse = addBook(request, session);
    out.print(jsonResponse);
  }
}
