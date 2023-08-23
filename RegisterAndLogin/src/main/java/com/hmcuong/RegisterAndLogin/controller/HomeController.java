package com.hmcuong.RegisterAndLogin.controller;

import com.hmcuong.RegisterAndLogin.DTO.UserDTO;
import com.hmcuong.RegisterAndLogin.entity.User;
import com.hmcuong.RegisterAndLogin.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final UserService userService;

    @ModelAttribute
    public void commonUser(Principal principal,Model model){
        if(principal!=null) {
            String email = principal.getName();
            User user = userService.findUserByEmail(email);
            model.addAttribute("user",user);
        }
    }

    @GetMapping("/")
    public String index(){
        return "index";
    }

    @GetMapping("/register")
    public String register(){
        return "register";
    }

    @GetMapping("/signin")
    public String login(){
        return "login";
    }

    @PostMapping("/saveUser")
    public String saveUser(@ModelAttribute UserDTO userDTO, HttpSession session, Model m, HttpServletRequest request){

        String url = request.getRequestURL().toString();  // http://localhost:8080/saveUser

        url = url.replace(request.getServletPath(),"");
        System.out.println(url);

        if(userService.findUserByEmail(userDTO.getEmail()) != null) {
            session.setAttribute("msg","This email already exists");
            return "redirect:/register";
        }

        // Save User to Database
        User u = new User();
        u.setName(userDTO.getName());
        u.setPassword(userDTO.getPassword());
        u.setEmail(userDTO.getEmail());
        User tmp = userService.saveUser(u,url);

        System.out.println(tmp);
        if(tmp!=null) {
            session.setAttribute("msg","Register successfully");
        }else {
            session.setAttribute("msg","Something went wrong");
        }
        return "redirect:/register";
    }

    @GetMapping("/verify")
    public String verifyAccount(@Param("code") String code,Model m){
        if(userService.verifyAccount(code)){
            m.addAttribute("msg","Successfully your account is verified");
        } else {
            m.addAttribute("msg","may by your verification code is incorrect or already verified");
        }
        return "message";
    }
}
