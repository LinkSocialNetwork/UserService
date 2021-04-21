package com.link.service;

import com.link.dao.UserDao;
import com.link.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("UserService")
public class UserServiceImpl implements UserService{
    UserDao userDao;

    /** Authors: Chris B, Christian K, Dang L, Nack H
     *
     * @param user user from the front end
     * @return full user object
     */
    @Override
    public User logIn(User user) {
        User fromDB= userDao.getUserByUserName(user.getUserName());
        if(user.getPassword().equals(fromDB.getPassword())){
            return fromDB;
        }
        return null;
    }

    @Override
    public void createUser(User user) {
        userDao.createUser(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    @Override
    public User getUserByID(int userID) {
        return userDao.getUserByID(userID);
    }

    @Override
    public User getUserByUserName(String userName) {
        return userDao.getUserByUserName(userName);
    }

    @Override
    public void updateUser(User user) {
        userDao.updateUser(user);
    }

    @Override
    public void deleteUser(User user) {
        userDao.deleteUser(user);
    }
}
