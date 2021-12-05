package com.dummyframework.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import com.dummyframework.core.bean.BeanFactory;
import com.dummyframework.exception.AppContextException;
import com.dummyframework.logger.Logger;

public class ApplicationContext {

  private Logger LOG = new Logger(ApplicationContext.class);
  private BeanFactory beanFactory = new BeanFactory();

  public ApplicationContext(Set<String> scannedClasses)
      throws ClassNotFoundException, AppContextException {
    try {
      LOG.info("Bean creation started.");
      beanFactory.createBeans(getClasses(scannedClasses));
      LOG.info("Application Context started.");
    } catch (Exception e) {
      throw new AppContextException("Cannot start App Context.");
    }
  }

  private List<Class<?>> getClasses(Set<String> scannedClass) throws ClassNotFoundException {
    List<Class<?>> classes = new ArrayList<>();
    for (String className : scannedClass) {
      classes.add(toClass(className));
    }
    return classes;
  }

  private Class<?> toClass(String className) throws ClassNotFoundException {
    return Class.forName(className);
  }
}
