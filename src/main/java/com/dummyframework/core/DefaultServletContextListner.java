package com.dummyframework.core;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import com.application.library.LibrarySystem;
import com.dummyframework.exception.AppContextException;
import com.dummyframework.exception.NoComponentScanException;

public class DefaultServletContextListner implements ServletContextListener {

  @Override
  public void contextInitialized(ServletContextEvent sce) {
    LibrarySystem librarySystem = new LibrarySystem();
    try {
      try {
        LibrarySystem.main(null);
      } catch (AppContextException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    } catch (ClassNotFoundException | IllegalArgumentException | IllegalAccessException | NoSuchMethodException
        | SecurityException | InstantiationException | InvocationTargetException | IOException
        | NoComponentScanException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void contextDestroyed(ServletContextEvent arg0) {

  }

}
