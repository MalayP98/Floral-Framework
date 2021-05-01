package com.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
}
