package com.hmcuong.RegisterAndLogin.service;

import com.hmcuong.RegisterAndLogin.entity.User;

public interface UserService {
    public User saveUser(User user,String url);
    public void removeSessionMessage();
    public User findUserByEmail(String email);
    public void sendEmail(User user,String url);
    public boolean verifyAccount(String verificationCode);
}
