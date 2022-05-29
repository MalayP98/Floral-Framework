package com.dummyframework.core;

import java.lang.reflect.InvocationTargetException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class DefaultServletContextListner implements ServletContextListener {

  @Override
  public void contextInitialized(ServletContextEvent sce) {
    try {
      run();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void run() throws ClassNotFoundException, NoSuchMethodException, SecurityException,
      IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    Class<?> mainClass = Class.forName(Properties.get(DefaultProperties.MAIN_CLASS_NAME));
    mainClass.getMethod("main", new Class<?>[0]).invoke(null, new Object[0]);
  }

  @Override
  public void contextDestroyed(ServletContextEvent arg0) {

  }

}
