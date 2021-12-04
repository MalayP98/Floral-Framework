package com.dummyframework.core;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.dummyframework.core.bean.Reader;
import com.dummyframework.exception.AppContextException;
import com.dummyframework.logger.Logger;

public class ApplicationContext {

  private Logger logger = new Logger(ApplicationContext.class);
  private Reader reader = new Reader();

  public ApplicationContext(Set<String> scannedClasses)
      throws ClassNotFoundException, AppContextException {

    Set<Class<?>> classes = getClasses(scannedClasses);
    try {
      logger.info("Application Context started.");
    } catch (Exception e) {
      throw new AppContextException("Cannot start App Context.");
    }
  }

  private Set<Class<?>> getClasses(Set<String> scannedClass) throws ClassNotFoundException {
    Set<Class<?>> classes = new HashSet<>();
    for (String className : scannedClass) {
      classes.add(toClass(className));
    }
    return classes;
  }

  private Class<?> toClass(String className) throws ClassNotFoundException {
    return Class.forName(className);
  }

}
