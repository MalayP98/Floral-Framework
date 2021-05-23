package com.application.library.models;

public class User {

  private int user_id;
  private String username;
  private String password;
  private String email;
  private ROLES role;
  private int issuedBooks;

  public User() {

  }

  public User(String username, String password, String email, ROLES role, int issuedBooks) {
    super();
    this.username = username;
    this.password = password;
    this.email = email;
    this.role = role;
    this.issuedBooks = issuedBooks;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public ROLES getRole() {
    return role;
  }

  public void setRole(ROLES role) {
    this.role = role;
  }

  public int getIssuedBooks() {
    return issuedBooks;
  }

  public void setIssuedBooks(int issuedBooks) {
    this.issuedBooks = issuedBooks;
  }
}
