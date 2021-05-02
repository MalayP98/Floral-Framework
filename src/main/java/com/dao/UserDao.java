package com.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import com.models.User;
import com.utils.Utils;

public class UserDao {

  public void closeAll(Connection connection, Statement statement, ResultSet resultSet)
      throws SQLException {
    resultSet.close();
    connection.close();
    statement.close();
  }

  public String getUsernameFromPassword(String password) throws SQLException {
    Connection connection = null;

    try {
      connection = Utils.getConnection("postgres", "admin", "xyz@123", "library_management_system");
    } catch (Exception e) {
      e.printStackTrace();
    }

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

  public List<Object> getRoleAndIdFromUsernameAndPassword(String username, String password)
      throws SQLException {
    Connection connection = null;
    List<Object> result = new ArrayList<Object>();

    try {
      connection = Utils.getConnection("postgres", "admin", "xyz@123", "library_management_system");
    } catch (Exception e) {
      e.printStackTrace();
    }

    String query = "select role, user_id from users where username=? and password=?";
    PreparedStatement preparedStatement = connection.prepareStatement(query);
    preparedStatement.setString(1, username);
    preparedStatement.setString(2, password);

    ResultSet resultSet = preparedStatement.executeQuery();
    if (resultSet.next()) {
      result.add(resultSet.getString("role"));
      result.add(resultSet.getString("user_id"));
    }
    closeAll(connection, preparedStatement, resultSet);
    return result;
  }

  public long addUser(User user) throws SQLException {
    Connection connection = null;

    try {
      connection = Utils.getConnection("postgres", "admin", "xyz@123", "library_management_system");
    } catch (Exception e) {
      e.printStackTrace();
    }

    String query =
        "insert into users (username, password, email, role, issued_books) values (?, ?, ?, ?, ?);";
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
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
        resultSet = preparedStatement.getGeneratedKeys();
        if (resultSet.next()) {
          id = resultSet.getLong(1);
        } else
          throw new SQLException();
      }
    } catch (SQLException e) {
      e.getMessage();
    }
    closeAll(connection, preparedStatement, resultSet);
    return id;
  }
}
