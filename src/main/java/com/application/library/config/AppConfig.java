package com.application.library.config;

import com.dummyframework.annotations.Config;
import com.dummyframework.annotations.Dependency;
import com.dummyframework.core.Properties;

@Config
public class AppConfig {

  public AppConfig() {
    System.out.println("\n\n **** AppConfig constructor called.");
  }

  public void setAppName() {
    Properties.set("APP_NAME", "app");
  }

  @Dependency
  public Model model() {
    System.out.println("\n inside app config creating model bean.");
    return new Model();
  }

}
