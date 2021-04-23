package com.link.dao;

import com.link.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface UserDao extends JpaRepository<User, Integer> {
    public User findByUserName(String userName);
}
