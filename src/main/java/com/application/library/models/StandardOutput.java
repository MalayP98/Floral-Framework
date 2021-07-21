package com.application.library.models;

import java.util.Date;

import com.dummyframework.annotations.Service;

@Service
public class StandardOutput {

  String status;
  String message;
  Date date;

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }
}
