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
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController{

    private final UserService userService;

    @GetMapping("/profile")
    public String profile(){
        return "admin_profile";
    }

    @ModelAttribute
    public void commonUser(Principal p, Model m) {
        if (p != null) {
            String email = p.getName();
            User user = userService.findUserByEmail(email);
            m.addAttribute("user", user);
        }
    }

}
