package com.application.library.controllers;

import com.dummyframework.annotations.Controller;
import com.dummyframework.annotations.RequestMapping;
import com.dummyframework.utils.RequestMethod;

@Controller
public class NewController {

  @RequestMapping(value = "/add", method = RequestMethod.GET, produces = "text/plain")
  public String add() {
    return "Hello World!";
  }

}
