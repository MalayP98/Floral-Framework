package com.application.library.config;

import com.dummyframework.annotations.Primary;
import com.dummyframework.annotations.Service;

@Primary
@Service
public class InterfcAImpl implements InterfcA {

  @Override
  public void disp() {
    System.out.println("\n\nfrom " + this.getClass().getName() + " \n\n");

  }

}
