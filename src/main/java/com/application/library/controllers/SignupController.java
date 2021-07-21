package com.application.library.controllers;

import static com.dummyframework.core.FrameworkSession.add;
import java.util.Date;
import java.util.List;
import com.application.library.dao.UserService;
import com.application.library.models.StandardOutput;
import com.application.library.models.User;
import com.application.library.utils.Constants;
import com.dummyframework.annotations.Autowired;
import com.dummyframework.annotations.Controller;
import com.dummyframework.annotations.RequestBody;
import com.dummyframework.annotations.RequestMapping;
import com.dummyframework.utils.RequestMethod;

@Controller
public class SignupController {

  @Autowired
  UserService userService;

  @Autowired
  StandardOutput standardOutput;

  @RequestMapping(value = "/signup", method = RequestMethod.POST, produces = "application/json")
  public StandardOutput signup(@RequestBody(tag = "user") User user) {
    try {
      userService.addUser(user);
      List<Object> result =
          userService.getRoleAndIdFromUsernameAndPassword(user.getUsername(), user.getPassword());
      add("id", result.get(1));
      add("role", result.get(0));
      standardOutput.setStatus(Constants.SUCCESS);
      standardOutput.setMessage("Signed Up.");
    } catch (Exception e) {
      standardOutput.setStatus(Constants.FAILED);
      standardOutput.setMessage("Error Occured, Try Again.");
    }
    standardOutput.setDate(new Date());
    return standardOutput;
  }
}
