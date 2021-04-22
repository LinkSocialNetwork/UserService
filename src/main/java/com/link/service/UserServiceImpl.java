package com.link.service;

import com.link.dao.UserDao;
import com.link.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("UserService")
public class UserServiceImpl implements UserService{
    UserDao userDao;

    /** Authors: Chris B, Christian K, Dang L, Nick H
     * This will compare the user password from front end and see if it match with back-end
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

    /** Authors: Chris B, Christian K, Dang L, Nick H
     * Creates new user in DB, pass in all user info except ID
     *
     * @param user All info except ID, that's generated in the DB.
     *
     */
    @Override
    public void createUser(User user) {
        userDao.createUser(user);
    }


    /** Authors: Chris B, Christian K, Dang L, Nick H
     * Getting all User as a list from database
     * And return all User
     * @param
     * @return All user
     */
    @Override
    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    /** Authors: Chris B, Christian K, Dang L, Nick H
     * Returns a specific user chosen by ID
     * @param userID target user's ID
     * @return the user with the matching ID
     */
    @Override
    public User getUserByID(int userID) {
        return userDao.getUserByID(userID);
    }

    /** Authors: Chris B, Christian K, Dang L, Nick H
     * Returns a specific user chosen by username
     * @param userName target user's username
     * @return the user with the matching username
     */
    @Override
    public User getUserByUserName(String userName) {
        return userDao.getUserByUserName(userName);
    }

    /** Authors: Chris B, Christian K, Dang L, Nick H
     * Replaces old user data in the DB with new user data given as input. Id should never change!
     * @param user The new data
     */
    @Override
    public void updateUser(User user) {
        userDao.updateUser(user);
    }

    /** Authors: Chris B, Christian K, Dang L, Nick H
     * Deleting user
     * @param user with current data
     */
    @Override
    public void deleteUser(User user) {
        userDao.deleteUser(user);
    }
}
