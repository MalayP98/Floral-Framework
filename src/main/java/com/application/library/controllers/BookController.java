package com.application.library.controllers;

import java.util.Date;
import com.application.library.service.BooksService;
import com.application.library.models.Book;
import com.application.library.models.StandardOutput;
import com.application.library.utils.Constants;
import com.dummyframework.annotations.Autowired;
import com.dummyframework.annotations.Controller;
import com.dummyframework.annotations.RequestBody;
import com.dummyframework.annotations.RequestMapping;
import com.dummyframework.annotations.ResponseBody;
import com.dummyframework.core.FrameworkSession;
import com.dummyframework.utils.RequestMethod;

@Controller
@ResponseBody
public class BookController {

  @Autowired
  BooksService booksService;

  @Autowired
  StandardOutput standardOutput;

  public boolean verify() {
    if (FrameworkSession.isNull()) {
      return false;
    }
    return (FrameworkSession.get("role").equals("admin")) ? true : false;
  }

  @RequestMapping(value = "/book", method = RequestMethod.POST, produces = "application/json")
  public StandardOutput addBook(@RequestBody(tag = "book") Book book) {
    boolean canAdd = false;
    try {
      canAdd = verify();
      if (canAdd) {
        try {
          booksService.addBook(book);
          standardOutput.setStatus(Constants.SUCCESS);
          standardOutput.setMessage("Book Added.");
        } catch (Exception e) {
          standardOutput.setStatus(Constants.FAILED);
          standardOutput.setMessage("Failed. Try Again.");
        }
      } else {
        standardOutput.setStatus(Constants.FAILED);
        standardOutput.setMessage("Authorization Failed.");
      }
      standardOutput.setDate(new Date());
    } catch (Exception e) {
      standardOutput.setStatus(Constants.FAILED);
      standardOutput.setMessage("User not logged in.");
    }
    return standardOutput;
  }

}
