package com.link.dao;

import com.link.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserDao extends JpaRepository<User, Integer> {
    public User findByUserName(String userName);
}
