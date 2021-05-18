package com.application.libary;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import com.dummyframework.annotations.ComponentScan;
import com.dummyframework.core.DummyFramework;
import com.dummyframework.exception.NoComponentScanException;

@ComponentScan
public class LibrarySystem {

  public static void main(String[] args) throws IOException, NoComponentScanException,
      ClassNotFoundException, IllegalArgumentException, IllegalAccessException,
      NoSuchMethodException, SecurityException, InstantiationException, InvocationTargetException {
    DummyFramework.run(LibrarySystem.class);
  }

}
