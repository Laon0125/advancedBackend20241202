package com.lion.demo.controller;

import com.lion.demo.entity.User;
import com.lion.demo.service.UserService;
import com.lion.demo.service.UserServiceImpl;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.PublicKey;
import java.sql.JDBCType;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;


@Controller
@RequestMapping("/user")
//@RequiredArgsConstructor
//@Slf4j
public class UserController {
    @Autowired
    private UserService userService;
//    private final User finalUser;
//    public userController(User finalUser){
//        this.finalUser = finalUser;
//    }


    @GetMapping("/register")
    public String registerForm() {
        return "user/register";
    }
    @PostMapping("/register")
    public String registerProc(String uid, String pwd,String pwd2, String uname, String email) {
        if ( userService.findByUid(uid) == null && pwd.equals(pwd2) && pwd.length() >= 4) {
            String hashedPWD = BCrypt.hashpw(pwd, BCrypt.gensalt());
            User user = User.builder()
                    .uid(uid).pwd(hashedPWD).uname(uname).email(email)
                    .registerDate(LocalDate.now())
                    .role("ROLE_USER")
                    .build();
            userService.registerUser(user);

        }
        return "redirect:/mall/list";
    }

    @GetMapping("/list")
    public String list(Model model) {
        List<User> userList = userService.getUsers();
        model.addAttribute("userList", userList);
        return "user/list";

    }
    @GetMapping("/delete/{uid}")
    public String uid(@PathVariable("uid") String uid) {
        System.out.println("========================" +uid);
        userService.deleteUser(uid);
        return "redirect:/user/list";
    }
    @GetMapping("/update/{uid}")
    public String updateForm(@PathVariable String uid, Model model) {
        User user = userService.findByUid(uid);
        model.addAttribute("user", user);
        return "user/update";
    }
    @PostMapping("/update")
    public String registerProc(@RequestParam("uid") String uid,
                               @RequestParam("pwd") String pwd,
                               @RequestParam("pwd2") String pwd2,
                               @RequestParam("uname") String uname,
                               @RequestParam("email") String email,
                               @RequestParam ("role") String role) throws Exception {
        User user = userService.findByUid(uid);
        if (pwd.equals(pwd2) && pwd.length() >= 4) {
            String hashedPWD = BCrypt.hashpw(pwd, BCrypt.gensalt());
            user.setPwd(hashedPWD);
        }
        user.setUname(uname);
        user.setEmail(email);
        user.setRole(role);
        userService.updateUser(user);
        return "redirect:/mall/list";

    }
    @GetMapping("/login")
    public String loginForm() {
        return "user/login";
    }
//    @PostMapping("/login")
//    public String loginProc (String uid, String pwd, HttpSession session, Model model){
//        String msg, url;
//        int result = userService.login(uid,pwd);
//        if (result == UserService.CORRECT_LOGIN){
//            User user = userService.findByUid(uid);
//            session.setAttribute("sessUid", uid);
//            session.setAttribute("sessUname", user.getUname());
//            msg = user.getUname() + "님 환영합니다." ;
//            url = "/mall/list";
//        } else if (result == UserService.WRONG_PASSWORD){
//            msg = "비밀번호가 틀림";
//            url = "login";
//        } else  {
//            msg = "아이디가 존재하지 않음";
//            url = "register";
//        }
//        model.addAttribute("msg",msg);
//        model.addAttribute("url", url);
//        return "common/alertMsg";
//    }
    @GetMapping("/loginSuccess")
    public String loginSuccess(HttpSession session, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String uid = authentication.getName();

        User user = userService.findByUid(uid);
        session.setAttribute("sessUid", uid);
        session.setAttribute("sessUname", user.getUname());
        String msg = user.getUname() + "님 환영합니다." ;
        String url = "/book/list";
        model.addAttribute("msg",msg);
        model.addAttribute("url", url);

        return "common/alertMsg";
    }
    @GetMapping
    public String logout() {
        return "redirect:/user/login";
    }
}
