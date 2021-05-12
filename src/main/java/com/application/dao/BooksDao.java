package com.application.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.application.models.Book;
import com.application.utils.Utils;

public class BooksDao {

  public long addBook(Book book) {
    Connection connection = null;

    try {
      connection = Utils.getConnection("postgres", "admin", "xyz@123", "library_management_system");
    } catch (Exception e) {
      e.printStackTrace();
    }

    String query = "insert into books (book_name, author, copies, isbn) values (?, ?, ?, ?)";
    PreparedStatement preparedStatement;
    long id = -1;
    try {
      preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
      preparedStatement.setString(1, book.getBookName());
      preparedStatement.setString(2, book.getAuthor());
      preparedStatement.setInt(3, book.getCopies());
      preparedStatement.setString(4, book.getIsbn());

      int affectedRows = preparedStatement.executeUpdate();
      if (affectedRows == 1) {
        ResultSet resultSet = preparedStatement.getGeneratedKeys();
        if (resultSet.next()) {
          id = resultSet.getLong(1);
        } else
          throw new SQLException();
      }
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }

    return id;
  }

  public void issuseBooks(String[] books, int user_id) throws SQLException {
    Connection connection = null;
    List<Integer> bookIds = new ArrayList<Integer>();
    PreparedStatement preparedStatement;
    ResultSet resultSet = null;

    try {
      connection = Utils.getConnection("postgres", "admin", "xyz@123", "library_management_system");
    } catch (Exception e) {
      e.printStackTrace();
    }

    String allBooks = "select book_id, copies from books where book_name=? and copies>0";
    String book_in_user = "insert into user_book (book_id, user_id) values (?, ?)";
    String updateCopies = "update books set copies=? where book_id=?";
    for (String book : books) {
      preparedStatement = connection.prepareStatement(allBooks);
      preparedStatement.setString(1, book);
      resultSet = preparedStatement.executeQuery();
      if (resultSet.next()) {
        int id = resultSet.getInt("book_id");
        int copies = resultSet.getInt("copies");
        preparedStatement =
            connection.prepareStatement(book_in_user, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setInt(1, id);
        preparedStatement.setInt(2, user_id);
        preparedStatement.executeUpdate();

        preparedStatement = connection.prepareStatement(updateCopies);
        preparedStatement.setInt(1, copies - 1);
        preparedStatement.setInt(2, id);
        preparedStatement.executeUpdate();
      }
    }
  }

  public void returnBook(String[] books, int user_id) throws SQLException {
    Connection connection = null;
    PreparedStatement preparedStatement;
    ResultSet resultSet = null;
    HashMap<String, List<Integer>> userBooks = new HashMap<String, List<Integer>>();

    try {
      connection = Utils.getConnection("postgres", "admin", "xyz@123", "library_management_system");
    } catch (Exception e) {
      e.printStackTrace();
    }

    String boodIdFromUserId = "select book_id from user_book where user_id=?";
    String bookFromBookId = "select book_name, copies from books where book_id=?";
    String removeBook = "delete from user_book where book_id=? and user_id=?";
    String addCopies = "update books set copies=? where book_id=?";

    preparedStatement = connection.prepareStatement(boodIdFromUserId);
    preparedStatement.setInt(1, user_id);
    resultSet = preparedStatement.executeQuery();
    while (resultSet.next()) {
      int book_id = resultSet.getInt("book_id");

      preparedStatement = connection.prepareStatement(bookFromBookId);
      preparedStatement.setInt(1, book_id);
      ResultSet resultSetForBookId = preparedStatement.executeQuery();
      if (resultSetForBookId.next()) {
        List<Integer> info = new ArrayList<Integer>();
        info.add(resultSetForBookId.getInt("copies"));
        info.add(book_id);
        userBooks.put(resultSetForBookId.getString("book_name"), info);
      }
    }

    for (String book : books) {
      if (userBooks.containsKey(book)) {
        List<Integer> info = userBooks.get(book);

        preparedStatement = connection.prepareStatement(addCopies);
        preparedStatement.setInt(1, info.get(0) + 1);
        preparedStatement.setInt(2, info.get(1));
        preparedStatement.executeUpdate();

        preparedStatement = connection.prepareStatement(removeBook);
        preparedStatement.setInt(1, info.get(1));
        preparedStatement.setInt(2, user_id);
        preparedStatement.executeUpdate();
      }
    }
  }
}
