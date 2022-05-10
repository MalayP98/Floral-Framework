package com.dummyframework.core.bean;

import java.util.HashMap;
import com.dummyframework.logger.Logger;

// stores object created by bean definitions.
public class BeanRegistry {

  Logger LOG = new Logger(getClass());

  private static BeanRegistry INSTANCE = null;

  private BeanRegistry() {
  }

  public static BeanRegistry getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new BeanRegistry();
    }
    return INSTANCE;
  }

  private HashMap<String, Object> beanRegistry = new HashMap<>();

}
