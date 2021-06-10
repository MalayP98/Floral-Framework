package com.application.library.controllers;

import com.application.library.models.LoginForm;
import com.dummyframework.annotations.Controller;
import com.dummyframework.annotations.RequestBody;
import com.dummyframework.annotations.RequestMapping;
import com.dummyframework.utils.RequestMethod;

@Controller
public class NewController {

  @RequestMapping(value = "/add", method = RequestMethod.POST, produces = "text/plain")
  public String add(@RequestBody(tag = "x") LoginForm x, @RequestBody(tag = "y") int y) {
    System.out.println(x);
    return String.valueOf(y);
  }

}
