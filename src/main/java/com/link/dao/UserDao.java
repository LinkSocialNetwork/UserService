package com.link.dao;

import com.link.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserDao extends JpaRepository<User, Integer> {
    public User findByUserName(String userName);
    public User findById(int id);
    public User findByEmail(String email);
}
