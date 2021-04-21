package com.link.controllers;

import com.link.model.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public class UserController {
    UserController userController;

    //login
    @PostMapping("/login")
    public User logIn(@RequestBody User user){
        //setHttpSession(userController.logIn(user))
        return new User();
    }

    //create user

    //get user

    //get all users

    //update user

    //delete user

    //reset password

    //log out

    //upload profile image (might be in update user)

    //get current user ???

    

}
