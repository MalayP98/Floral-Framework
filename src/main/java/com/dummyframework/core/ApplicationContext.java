package com.dummyframework.core;

import java.util.List;
import com.dummyframework.core.bean.BeanFactory;
import com.dummyframework.core.handler.HandlerMapper;
import com.dummyframework.exception.AppContextException;
import com.dummyframework.logger.Logger;

public class ApplicationContext {

  private Logger LOG = new Logger(ApplicationContext.class);

  private BeanFactory beanFactory = new BeanFactory();

  private HandlerMapper handlerMapper = new HandlerMapper();

  public ApplicationContext(List<Class<?>> scannedClasses)
      throws ClassNotFoundException, AppContextException {
    try {
      LOG.info("Bean creation started.");
      beanFactory.createBeans(scannedClasses);
      handlerMapper.map(scannedClasses);
      LOG.info("Application Context started.");
    } catch (Exception e) {
      throw new AppContextException("Cannot start App Context.");
    }
  }
}
