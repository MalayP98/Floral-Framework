package com.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import com.models.Book;
import com.utils.Utils;

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

    try {
      connection = Utils.getConnection("postgres", "admin", "xyz@123", "library_management_system");
    } catch (Exception e) {
      e.printStackTrace();
    }

    String allBooks = "select book_id, copies from books where book_name=? and copies>0";
    String book_in_user = "insert into user_book (book_id, user_id) values (?, ?)";
    String updateCopies = "update books set copies=? where book_id=?";
    PreparedStatement preparedStatement;
    ResultSet resultSet = null;
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

    try {
      connection = Utils.getConnection("postgres", "admin", "xyz@123", "library_management_system");
    } catch (Exception e) {
      e.printStackTrace();
    }

    String allBooks = "select book_id, copies from books where book_name=?";
    String deleteBook = "delete from user_book where user_id=? and book_id=?";
    String incrementCopies = "update books set copies=? where book_id=?";
    PreparedStatement preparedStatement;
    ResultSet resultSet = null;
    for (String book : books) {
      preparedStatement = connection.prepareStatement(allBooks);
      preparedStatement.setString(1, book);
      resultSet = preparedStatement.executeQuery();
      if (resultSet.next()) {
        int id = resultSet.getInt("book_id");
        int copies = resultSet.getInt("copies");

        preparedStatement = connection.prepareStatement(deleteBook);
        preparedStatement.setInt(1, user_id);
        preparedStatement.setInt(2, id);
        preparedStatement.executeUpdate();

        preparedStatement = connection.prepareStatement(incrementCopies);
        preparedStatement.setInt(1, copies + 1);
        preparedStatement.setInt(2, id);
        preparedStatement.executeUpdate();
      }
    }
  }
}
