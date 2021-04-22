package com.link.dao;

import com.link.model.User;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import javax.validation.constraints.Null;
import java.util.List;

@Transactional
@Repository("userRepo")
public class UserDaoImpl implements UserDao{

    private SessionFactory sesFact;

    /** Authors: Chris B, Christian K, Dang L, Nick H
     * Creates new user in DB, pass in all user info except ID
     *
     * @param user All info except ID, that's generated in the DB.
     */


    @Override
    public void createUser(User user){
        sesFact.getCurrentSession().save(user);
    }

/** Authors: Chris B, Christian K, Dang L, Nick H
     * Returns a specific user chosen by ID
     * @param userID target user's ID
     * @return the user with the matching ID
  */


    @Override
    public User getUserByID(int userID){
        return sesFact.getCurrentSession().get(User.class, userID);
    }

/** Authors: Chris B, Christian K, Dang L, Nick H
     * Returns a specific user chosen by username
     * @param userName target user's username
     * @return the user with the matching username */



    @Override
    public User getUserByUserName(String userName) {
        List<User> user= sesFact.getCurrentSession().createQuery("from User where username= '"+ userName +"'").list();
        User output;
        try{output= user.get(0);}
        catch (NullPointerException e){
            //log search by user name returned no results
            return null;
        }
        return output;
    }

/** Authors: Chris B, Christian K, Dang L, Nick H
     * Getting all User as a list from database
     * And return all User
     * @param
     /* @return All user */


    @Override
    public List<User> getAllUsers() {
        return sesFact.getCurrentSession().createQuery("from UserAccount", User.class).list();
    }

/** Authors: Chris B, Christian K, Dang L, Nick H
     * Replaces old user data in the DB with new user data given as input. Id should never change!
     * @param user The new data */


    @Override
    public void updateUser(User user){
        sesFact.getCurrentSession().update(user);
    }

/** Authors: Chris B, Christian K, Dang L, Nick H
     * Deleting user
     * @param user with current data */


    @Override
    public void deleteUser(User user){
        sesFact.getCurrentSession().delete(user);
    }





    @Autowired
    public void setSesFact(SessionFactory sesFact){
        this.sesFact= sesFact;
    }

    public UserDaoImpl(SessionFactory sesFact) {
        this.sesFact = sesFact;
    }

    public UserDaoImpl() {
    }
}
