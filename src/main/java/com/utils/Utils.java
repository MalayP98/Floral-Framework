package com.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;

public class Utils {

  public static Connection getConnection(String database, String user, String password,
      String dbName) throws SQLException, ClassNotFoundException {
    Connection connection = null;
    String url = "";
    if (database.equals("postgres")) {
      Class.forName("org.postgresql.Driver");
      try {
        url = "jdbc:postgresql://localhost:5432/" + dbName;
        connection = DriverManager.getConnection(url, user, password);
      } catch (Exception e) {
        System.out.print(e.getMessage());
      }
    }
    return connection;
  }

  public static String getBody(HttpServletRequest request) throws IOException {
    StringBuilder stringBuilder = new StringBuilder();
    BufferedReader reader = request.getReader();
    String line = reader.readLine();
    while (line != null) {
      stringBuilder.append(line);
      line = reader.readLine();
    }
    return stringBuilder.toString();
  }
}
