package com.link.controllers;

import com.link.model.User;
import com.link.service.JWTServiceImpl;
import com.link.service.UserServiceImpl;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@CrossOrigin
@RequestMapping("/account")
public class AccountController {

    private UserServiceImpl userService;
    private JWTServiceImpl jwtService;

    final static Logger loggy = Logger.getLogger(AccountController.class);
    static {
        loggy.setLevel(Level.ALL);
        //loggy.setLevel(Level.ERROR);
    }

    /**
     * Api endpoint for the main landing page of application that allows user to login.
     * @param session HTTP session
     * @param user User object of the current logged in user.
     * @return User object
     */
    //TODO: JWTServiceImpl has all the auth token logic, this just needs to call generateToken(user) to make a new one
    @PostMapping("/login")
    public User login(HttpSession session, @RequestBody User user){
        /*
        since the password is now encrypted the new user must be retrieved by
        its username instead of by a dummy user object, as it was pre pw hashing.
        */
        User newUser= userService.getUserByUserName(user.getUserName());

        //check if the PW input on client side matches the encrypted PW in our DB
//        if(passwordEncoder.matches(user.getPassword(),newUser.getPassword())){
//            //same login logic as before password encryption
//            session.setAttribute("loggedInUser", newUser);
//
//            User currentUser = (User) session.getAttribute("loggedInUser");
//            return newUser;
//
//        }else return new User();        // should fix this that return empty user

        //TODO This used to use Spring Security's PasswordEncoder interface, but we don't need it
        return null;
    }

    /**
     * Api endpoint that logs out the user from the application. Redirects to landing page.
     * @param myReq HTTP servlet request
     */
    //TODO: might change the session into auth token
    @GetMapping(value = "/logout")
    public void logout(HttpServletRequest myReq){

        //TODO: need to refactor from auth token
        HttpSession userSession = myReq.getSession();
        loggy.info("The successful logout of the session:"+userSession);
        userSession.invalidate();
    }





    public AccountController() {
    }

    @Autowired
    public AccountController(UserServiceImpl userService, JWTServiceImpl jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }
}
