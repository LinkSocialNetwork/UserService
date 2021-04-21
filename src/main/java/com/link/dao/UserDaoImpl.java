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

    @Override
    public void createUser(User user){
        sesFact.getCurrentSession().save(user);
    }

    @Override
    public User getUserByID(int userID){
        return sesFact.getCurrentSession().get(User.class, userID);
    }

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

    @Override
    public List<User> getAllUsers() {
        return sesFact.getCurrentSession().createQuery("from UserAccount", User.class).list();
    }

    @Override
    public void updateUser(User user){
        sesFact.getCurrentSession().update(user);
    }

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
