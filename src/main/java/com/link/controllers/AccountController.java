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
     * Api endpoint that inserts User object into the application depending on whether they exist or not.
     * @param user User object.
     * @return Custom response message (string).
     */
    @PostMapping(value = "/insertNewUser2")
    public void insertNewUser(@RequestBody User user)
    {
        User alreadyExists = userService.getUserByUserName(user.getUserName());

        if(alreadyExists == null)
        {

            // This will hash the password and set it to the user before sending it to the db
            String inputPass = user.getPassword();
            System.out.println("firstpassword:" + inputPass);
            inputPass = HashPassword.hashPassword(inputPass);
            System.out.println("secondpassword:" + inputPass);
            user.setPassword(inputPass);

            //When a user is created it will ping the post service to create a user also
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.postForEntity("http://localhost:9080/api/postservice/duplicateUser",user, User.class);

            userService.createUser(user);

            loggy.info("The successful creation of a user with username: "+user.getUserName()+".");

            //TODO Redirect to login/frontend
        }
        else {
            loggy.info("The failed creation of a user with username: "+user.getUserName()+".");
        }
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
    public String login(@RequestBody User user) throws Exception
    {
        User newUser = userService.getUserByUserName(user.getUserName());

        if (newUser == null)
        {
            loggy.info("Login: can't find username! Received: " + user.getUserName());
            return null;
        }

        String entered = user.getPassword();
        if(authorizer.authenticate(entered, newUser.getPassword())) {
            return JwtEncryption.encrypt(user);
        }
        else {
            loggy.info("Login: can't authenticate password! Received: " + user.getUserName());
            return null;
        }

    }

    /**
     * Api endpoint that logs out the user from the application. Redirects to landing page.
     * @param myReq HTTP servlet request
     */
    //TODO: We need to parse a User @RequestBody and have the front end send the user object
    @GetMapping(value = "/logout")
    public void logout(HttpServletRequest myReq)
    {
        HttpSession userSession = myReq.getSession();

        // This will set the current JWT auth token to null and update the db
        // TODO the session's won't work, so we need to get the user by their authtoken or username
        User currentUser = (User) userSession.getAttribute("loggedInUser");
        currentUser.setAuthToken(null);
        userService.updateUser(currentUser);

        loggy.info("The successful logout of the session:"+userSession);
        userSession.invalidate();
    }

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
        //this.jwtService = new JwtEncryption();
        this.authorizer = new PasswordAuthentication();
    }
}
