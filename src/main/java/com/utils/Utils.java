package com.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;

public class Utils {

  public static HashMap<String, Object> getMapResponse(String status, String message) {
    HashMap<String, Object> response = new HashMap<String, Object>();
    response.put(Constants.STATUS, status);
    response.put(Constants.MESSAGE, message);
    response.put(Constants.TIMESTAMP, new Date());
    return response;
  }

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
      stringBuilder.append(line + ";");
      line = reader.readLine();
    }
    return stringBuilder.toString();
  }

  public static HashMap<String, Object> extractData(HttpServletRequest request) throws IOException {
    HashMap<String, Object> map = new HashMap<String, Object>();
    String body = getBody(request);
    String[] elements = body.split(";");
    for (String element : elements) {
      String[] pair = element.split("=");
      if (pair[1].split(",").length == 1)
        map.put(pair[0].trim(), pair[1].trim());
      else
        map.put(pair[0].trim(), pair[1].split(","));
    }
    return map;
  }
}
