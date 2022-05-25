package com.application.library.controllers;

import static com.dummyframework.core.FrameworkSession.add;
import java.util.Date;
import java.util.List;

import com.application.library.models.LoginForm;
import com.application.library.models.StandardOutput;
import com.application.library.service.UserService;
import com.application.library.utils.Constants;
import com.dummyframework.annotations.Autowired;
import com.dummyframework.annotations.Controller;
import com.dummyframework.annotations.RequestBody;
import com.dummyframework.annotations.RequestMapping;
import com.dummyframework.annotations.ResponseBody;
import com.dummyframework.utils.RequestMethod;

@Controller
@ResponseBody
public class LoginController {

  @Autowired
  UserService userService;

  @Autowired
  StandardOutput standardOutput;

  @RequestMapping(value = "/app/login", method = RequestMethod.POST, produces = "application/json")
  public String login(@RequestBody(tag = "loginForm") LoginForm loginForm) {
    String username = loginForm.getName();
    String password = loginForm.getPassword();
    String dbUsername = "";
    try {
      dbUsername = userService.getUsernameFromPassword(password);
      List<Object> result = userService.getRoleAndIdFromUsernameAndPassword(dbUsername, password);
      add("id", result.get(1));
      add("role", result.get(0));
    } catch (Exception e) {
      standardOutput.setStatus(Constants.FAILED);
      standardOutput.setMessage("Invalid credentials.");
    }
    if (username.equals(dbUsername)) {
      standardOutput.setStatus(Constants.SUCCESS);
      standardOutput.setMessage("Logged In.");
    } else {
      standardOutput.setStatus(Constants.FAILED);
      standardOutput.setMessage("Invalid credentials.");
    }
    standardOutput.setDate(new Date());
    return standardOutput.toString();
  }
}
