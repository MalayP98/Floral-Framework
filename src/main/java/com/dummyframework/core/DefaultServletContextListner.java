package com.dummyframework.core;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.application.library.LibrarySystem;
import com.dummyframework.exception.AppContextException;

public class DefaultServletContextListner implements ServletContextListener {

  @Override
  public void contextInitialized(ServletContextEvent sce) {
    try {
      try {
        LibrarySystem.main(null);
      } catch (AppContextException e) {
        e.printStackTrace();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void contextDestroyed(ServletContextEvent arg0) {

  }

}
