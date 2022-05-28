package com.application.library.controllers;

import com.application.library.models.LoginForm;
import com.dummyframework.annotations.Controller;
import com.dummyframework.annotations.PathVariable;
import com.dummyframework.annotations.QueryParameter;
import com.dummyframework.annotations.RequestBody;
import com.dummyframework.annotations.RequestMapping;
import com.dummyframework.annotations.ResponseBody;
import com.dummyframework.utils.RequestMethod;

@Controller
public class NewController {

  @RequestMapping(value = "/some/{id1}", method = RequestMethod.GET, produces = "text/plain")
  @ResponseBody
  public String add(@PathVariable(name = "id1") int id1, @QueryParameter(name = "x") int x,
      @RequestBody LoginForm form1, @RequestBody LoginForm form2) {
    System.out.println(form1);
    System.err.println(form2);
    System.out.println(id1 + " " + x + "\n");
    System.out.println("\nIts working\n");
    return "Hello World!";
  }

}
