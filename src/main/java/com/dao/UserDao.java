package com.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserDao {

  public String getUsernameFromPassword(Connection connection, String password)
      throws SQLException {
    String query = "select username from users where password='" + password + "';";
    Statement statement = connection.createStatement();
    ResultSet resultSet = statement.executeQuery(query);
    if (resultSet.next()) {
      return resultSet.getString("username");
    }
    return null;
  }
}
