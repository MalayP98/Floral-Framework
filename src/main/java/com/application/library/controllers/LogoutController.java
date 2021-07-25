package com.application.library.controllers;

import java.util.Date;
import com.application.library.models.StandardOutput;
import com.application.library.utils.Constants;
import com.dummyframework.annotations.Autowired;
import com.dummyframework.annotations.Controller;
import com.dummyframework.annotations.RequestMapping;
import com.dummyframework.annotations.ResponseBody;
import com.dummyframework.core.FrameworkSession;
import com.dummyframework.utils.RequestMethod;

@Controller
@ResponseBody
public class LogoutController {

  @Autowired
  StandardOutput standardOutput;

  @RequestMapping(value = "/logout", method = RequestMethod.GET, produces = "application/json")
  public StandardOutput logout() {
    FrameworkSession.invalidate();
    standardOutput.setStatus(Constants.SUCCESS);
    standardOutput.setMessage("Logged Out.");
    standardOutput.setDate(new Date());
    return standardOutput;
  }

}
