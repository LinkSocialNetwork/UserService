package com.link.dao;

import com.link.model.User;
import org.springframework.data.repository.CrudRepository;


import java.util.List;

public interface UserDao extends CrudRepository<User, Integer> {
/*    public void createUser(User user);
    public User getUserByID(int userID);
    public User getUserByUserName(String userName);
    public List<User> getAllUsers();
    public void updateUser(User user);
    public void deleteUser(User user);*/
    public User findByUserName(String userName);
}
