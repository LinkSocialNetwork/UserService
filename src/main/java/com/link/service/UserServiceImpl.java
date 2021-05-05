package com.link.service;

import com.link.dao.UserDao;

import com.link.model.User;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;


import org.springframework.stereotype.Service;
import com.link.model.User;

import java.util.List;

@Service("UserService")
public class UserServiceImpl implements UserService{
    UserDao userDao;

    @Autowired
    public UserServiceImpl(UserDao userDao){
        this.userDao = userDao;
    }

    /** Authors: Chris B, Christian K, Dang L, Nick H
     * This will compare the user password from front end and see if it match with back-end
     *
     * @param user user from the front end
     * @return full user object
     */
    @Override
    public User logIn(User user) {

        User fromDB= userDao.findByUserName(user.getUserName());
        if(user.getPassword().equals(fromDB.getPassword())){
            return fromDB;
        }
        return null;
    }

    /** Authors: Chris B, Christian K, Dang L, Nick H
     * Creates new user in DB, pass in all user info except ID
     *
     * @param user All info except ID, that's generated in the DB.
     *
     */
    @Override
    public void createUser(User user) {
        System.out.println("SERVICE: " + user);
        userDao.save(user);
    }


    /** Authors: Chris B, Christian K, Dang L, Nick H
     * Getting all User as a list from database
     * And return all User
     * @param
     * @return All user
     */
    @Override
    public List<User> getAllUsers() {
        return userDao.findAll();
    }

    /** Authors: Chris B, Christian K, Dang L, Nick H
     * Returns a specific user chosen by ID
     * @param userID target user's ID
     * @return the user with the matching ID
     */
    @Override
    public User getUserByID(int userID) {
        return userDao.findById(userID);
    }

    /** Authors: Chris B, Christian K, Dang L, Nick H
     * Returns a specific user chosen by username
     * @param userName target user's username
     * @return the user with the matching username
     */
    @Override
    public User getUserByUserName(String userName) {
        // Added a try catch so if it can't find the user
        // it returns null
        try {
            return userDao.findByUserName(userName);
        }
        catch (Exception e)
        {
            return null;
        }
    }

    /** Authors: Chris B, Christian K, Dang L, Nick H
     * Replaces old user data in the DB with new user data given as input. Id should never change!
     * @param user The new data
     */
    @Override
    public void updateUser(User user) {
        userDao.save(user);
    }

    /** Authors: Chris B, Christian K, Dang L, Nick H
     * Deleting user
     * @param userId with current data
     */
    @Override
    public void deleteUser(int userId) {
        userDao.deleteById(userId);
    }

    @Override
    public User getUserByEmail(String email) {
        return this.userDao.findByEmail(email);
    }
}
