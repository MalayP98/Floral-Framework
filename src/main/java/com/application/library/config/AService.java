package com.application.library.config;

import com.dummyframework.annotations.Autowired;
import com.dummyframework.annotations.Service;

@Service
public class AService {

  @Autowired
  Model model;

  public AService() {
    System.out.println("\n *** Service Class created *** \n");
  }

}
