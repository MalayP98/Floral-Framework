package com.application.library.config;

import com.dummyframework.annotations.Service;

@Service(name = "abc")
public class Model {

  public Model() {
    System.out.println("\n\n Model is created \n\n");
  }

  int i;
  int j;
  String isDelete;
  String IsDelete;

  public String isDelete() {
    return isDelete;
  }

  public void setDelete(String isDelete) {
    this.isDelete = isDelete;
  }

  public String isIsDelete() {
    return IsDelete;
  }

  public void setIsDelete(String IsDelete) {
    IsDelete = isDelete;
  }

  public int getI() {
    return i;
  }

  public void setI(int i) {
    this.i = i;
  }

  public int getJ() {
    return j;
  }

  public void setJ(int j) {
    this.j = j;
  }
}
