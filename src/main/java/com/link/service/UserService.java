package com.link.service;

import com.link.model.User;

import java.util.List;

public interface UserService {
    public User logIn(User user);
    public void createUser(User user);
    public List<User> getAllUsers();
    public User getUserByID(int userID);
    public User getUserByUserName(String userName);
    public void updateUser(User user);
    public void deleteUser(int userId);
    public User getUserByEmail(String email);
}
