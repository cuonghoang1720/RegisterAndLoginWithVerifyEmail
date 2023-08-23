package com.hmcuong.RegisterAndLogin.controller;

import com.hmcuong.RegisterAndLogin.entity.User;
import com.hmcuong.RegisterAndLogin.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    public String profile(Principal p, Model model){
        String email = p.getName();
        User user = userService.findUserByEmail(email);
        model.addAttribute("user",user);
        return "profile";
    }

    @ModelAttribute
    public void commonUser(Principal p, Model m) {
        if (p != null) {
            String email = p.getName();
            User user = userService.findUserByEmail(email);
            m.addAttribute("user", user);
        }
    }

    @GetMapping("/home")
    public String home(){
        return "home";
    }
}
