package com.application.library.controllers;

import java.util.Date;
import java.util.List;
import com.application.library.dao.BooksDao;
import com.application.library.dao.UserDao;
import com.application.library.models.StandardOutput;
import com.application.library.utils.Constants;
import com.dummyframework.annotations.Autowired;
import com.dummyframework.annotations.Controller;
import com.dummyframework.annotations.RequestBody;
import com.dummyframework.annotations.RequestMapping;
import com.dummyframework.core.FrameworkSession;
import com.dummyframework.utils.RequestMethod;


@Controller
public class IssueController {

  @Autowired
  UserDao userDao;

  @Autowired
  BooksDao bookDao;

  @Autowired
  StandardOutput standardOutput;

  @RequestMapping(value = "/issueBooks", method = RequestMethod.POST, produces = "application/json")
  public StandardOutput issueBook(@RequestBody(tag = "books") List<String> books) {
    boolean canIssue = FrameworkSession.isNull();
    canIssue = true;
    if (canIssue) {
      try {
        bookDao.issuseBooks(books, Integer.parseInt((String) FrameworkSession.get("id")));
        standardOutput.setStatus(Constants.SUCCESS);
        standardOutput.setMessage("Book Issued.");
      } catch (Exception e) {
        standardOutput.setStatus(Constants.FAILED);
        standardOutput.setMessage("Failed. Try Again.");
      }
    } else {
      standardOutput.setStatus(Constants.FAILED);
      standardOutput.setMessage("Login to issue books.");
    }
    standardOutput.setDate(new Date());
    return standardOutput;
  }

  @RequestMapping(value = "/returnBooks", method = RequestMethod.POST,
      produces = "application/json")
  public StandardOutput returnBook(@RequestBody(tag = "books") List<String> books) {
    boolean canIssue = FrameworkSession.isNull();
    canIssue = true;
    if (canIssue) {
      try {
        bookDao.returnBook(books, Integer.parseInt((String) FrameworkSession.get("id")));
        standardOutput.setStatus(Constants.SUCCESS);
        standardOutput.setMessage("Book Returned.");
      } catch (Exception e) {
        standardOutput.setStatus(Constants.FAILED);
        standardOutput.setMessage("Failed. Try Again.");
      }
    } else {
      standardOutput.setStatus(Constants.FAILED);
      standardOutput.setMessage("Login to issue books.");
    }
    standardOutput.setDate(new Date());
    return standardOutput;
  }
}
