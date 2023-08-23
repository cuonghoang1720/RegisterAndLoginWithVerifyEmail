package com.hmcuong.RegisterAndLogin.service;

import com.hmcuong.RegisterAndLogin.entity.User;
import com.hmcuong.RegisterAndLogin.repository.UserRepository;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    private final JavaMailSender mailSender;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    public User saveUser(User user,String url) {
        String password = passwordEncoder().encode(user.getPassword());
        user.setPassword(password);
        user.setRole("ROLE_ADMIN");
        user.setEnable(false);
        user.setVerificationCode(UUID.randomUUID().toString());
        System.out.println(UUID.randomUUID().toString());

        User newUser = userRepository.save(user);

        if(newUser != null) {
            sendEmail(newUser,url);
            System.out.println(" already sent email !");
        }

        return newUser;
    }

    @Override
    public void removeSessionMessage() {
        HttpSession session = ((ServletRequestAttributes)(Objects.requireNonNull(RequestContextHolder.getRequestAttributes()))).getRequest().getSession();
        session.removeAttribute("msg");
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void sendEmail(User user, String url) {
        String from = "cuonghoang1720@gmail.com";
        String to = user.getEmail();
        String subject = "Account Verification";
        String content = "Dear [[name]],<br>" + "Please click the link bellow to verify your registration:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>" + "Thank you,<br>" + "Hoang Cuong";
        try {
            MimeMessage message =mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);

            helper.setFrom(from,"Hoang Cuong");
            helper.setTo(to);
            helper.setSubject(subject);

            content =content.replace("[[name]]",user.getName());
            String siteUrl = url +"/verify?code=" + user.getVerificationCode();

            System.out.println(siteUrl);

            content = content.replace("[[URL]]", siteUrl);

            helper.setText(content,true);

            mailSender.send(message);

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean verifyAccount(String verificationCode) {
        User user = userRepository.findByVerificationCode(verificationCode);
        if(user == null) {
            return false;
        } else {
            user.setEnable(true);
            user.setVerificationCode(null);
            userRepository.save(user);
            return true;
        }
    }
}
