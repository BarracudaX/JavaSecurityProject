package controllers;

import dao.Comments;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@org.springframework.stereotype.Controller
@RequestMapping("/user")
public class UserController {

    private final Comments commentsDao;

    public UserController(Comments commentsDao) {
        this.commentsDao = commentsDao;
    }

    @GetMapping("/xss")
    public String xss(Model model) {
        model.addAttribute("comments", commentsDao.comments());
        return "user/xss";
    }

    @PostMapping("/xss")
    public String addComment(@RequestParam("comment") String comment,Model model) {
        commentsDao.addComment(comment);
        model.addAttribute("comments",  commentsDao.comments());
        return "user/xss";
    }

}
