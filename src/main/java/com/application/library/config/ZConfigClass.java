package com.application.library.config;

import com.dummyframework.annotations.Autowired;
import com.dummyframework.annotations.Config;
import com.dummyframework.annotations.Dependency;
import com.dummyframework.core.DefaultProperties;
import com.dummyframework.core.Properties;

@Config
public class ZConfigClass {

  public ZConfigClass() {
    System.out.println("\n\n **** AppConfig constructor called-------@@@@.");
  }

  @Autowired
  public void setAppName() {
    Properties.set(DefaultProperties.APP_NAME, "app");
  }

  @Dependency
  @Autowired
  public Model model() {
    System.out.println("\n inside app config creating model bean.");
    return new Model();
  }
}
