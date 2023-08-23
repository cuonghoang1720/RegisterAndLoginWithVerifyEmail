package com.hmcuong.RegisterAndLogin.repository;

import com.hmcuong.RegisterAndLogin.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    public User findByEmail(String email);
    User findByVerificationCode(String verificationCode);
}