package com.link.controllers;

import com.link.model.User;
import com.link.service.JWTServiceImpl;
import com.link.service.UserServiceImpl;
import com.link.util.JwtEncryption;
import com.link.util.HashPassword;
import com.link.util.PasswordAuthentication;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/api/userservice")
public class AccountController {

    private UserServiceImpl userService;
    private JwtEncryption jwtService;
    private PasswordAuthentication authorizer;

    final static Logger loggy = Logger.getLogger(AccountController.class);
    static {
        loggy.setLevel(Level.ALL);
        //loggy.setLevel(Level.ERROR);
    }

    /**
     * Api endpoint for the main landing page of application that allows user to login.
     *
     * Uses PasswordAuthentication to check if the entered password matches the hashed password in the db
     *
     * Generates a new JWT if there's not a valid one already
     *
     * @param user User object of the current logged in user.
     * @return User object
     */
    @PostMapping("/login")

    public User login(@RequestBody User user) throws Exception
    {

        User newUser = userService.getUserByUserName(user.getUserName());

        if (newUser == null)
        {
            loggy.info("Login: can't find username! Received: " + user.getUserName());
            return null;
        }

        String entered = user.getPassword();
        if(HashPassword.hashPassword(entered).equals(newUser.getPassword())) {
            newUser.setAuthToken(JwtEncryption.encrypt(newUser));
            return newUser;
        }
        else {
            loggy.info("Login: can't authenticate password! Received: " + user.getUserName());
            return null;
        }

    }

    /**
     * Api endpoint that logs out the user from the application. Redirects to landing page.
     * @param user User Object that represents who needs to be logged out
     */
//    TODO: We need to parse a User @RequestBody and have the front end send the user object
//    public void logout(User user) throws UnsupportedEncodingException {
//
//
//        User currentUser = userService.getUserByUserName(user.getUserName());
//        // This will set the current JWT auth token to null and update the db
//        // TODO the session's won't work, so we need to get the user by their authtoken or username
//        String authToken = currentUser.getAuthToken();
//        System.out.println(authToken);
//        User tokenUser = JwtEncryption.decrypt(authToken);
//        System.out.println(tokenUser);
//
//        //user.setAuthToken(null);
//        //userService.updateUser(user);
//
//    }

    /**
     * Get mapping to check if a user object's token is valid
     *
     * @param token the User to check
     * @return if there's a valid token
     */
    @GetMapping(value = "/checkToken")
    public User checkToken(@RequestHeader("token") String token) throws Exception
    {
        if(token == null)
        {
            //return jwtService.checkToken(user.getAuthToken());
            return null;
        }

        return JwtEncryption.decrypt(token);

    }





    public AccountController() {
    }

    @Autowired
    public AccountController(UserServiceImpl userService) {
        this.userService = userService;
    }
}
