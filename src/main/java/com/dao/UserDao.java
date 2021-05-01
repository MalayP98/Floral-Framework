package com.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import com.models.User;

public class UserDao {

  public void closeAll(Connection connection, Statement statement, ResultSet resultSet)
      throws SQLException {
    resultSet.close();
    connection.close();
    statement.close();
  }

  public String getUsernameFromPassword(Connection connection, String password)
      throws SQLException {
    String result = "";
    String query = "select username from users where password='" + password + "';";
    Statement statement = connection.createStatement();
    ResultSet resultSet = statement.executeQuery(query);
    if (resultSet.next()) {
      result = resultSet.getString("username");
    }
    closeAll(connection, statement, resultSet);
    return result;
  }

  public long addUser(Connection connection, User user) {
    String query =
        "insert into users (username, password, email, role, issued_books) values (?, ?, ?, ?, ?)";
    PreparedStatement preparedStatement;
    long id = -1;
    try {
      preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
      preparedStatement.setString(1, user.getUsername());
      preparedStatement.setString(2, user.getPassword());
      preparedStatement.setString(3, user.getEmail());
      preparedStatement.setString(4, user.getRole().toString());
      preparedStatement.setInt(5, 0);

      int affectedRows = preparedStatement.executeUpdate();
      if (affectedRows == 1) {
        ResultSet resultSet = preparedStatement.getGeneratedKeys();
        if (resultSet.next()) {
          id = resultSet.getLong(1);
        } else
          throw new SQLException();
      }
    } catch (SQLException e) {
      e.getMessage();
    }
    return id;
  }
}
