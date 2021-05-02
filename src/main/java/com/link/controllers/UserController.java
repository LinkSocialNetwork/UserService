package com.link.controllers;


import com.link.model.CustomResponseMessage;
import com.link.model.User;
import com.link.service.UserService;
import com.link.service.UserServiceImpl;
import com.link.util.HashPassword;
import com.link.util.JwtEncryption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Path;
import java.util.List;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.client.RestTemplate;


@RestController
@RequestMapping("/api/userservice")
public class UserController {

    private JavaMailSender mailSender;
    //    UserController userController;
    private UserService userService;
    final static Logger loggy = Logger.getLogger(UserController.class);
    static {
        loggy.setLevel(Level.ALL);
        //loggy.setLevel(Level.ERROR);
    }

    public UserController(UserService userService) {
        this.userService = userService;
    }
//----------------------------------------------------------------------------------------------//

    /**
     * Api endpoint that inserts User object into the application depending on whether they exists or not.
     * @param user User object.
     * @return Custom response message (string).
     */
    @PostMapping(value = "/user")
    public CustomResponseMessage insertNewUser(@RequestBody User user){
        User alreadyExists = userService.getUserByUserName(user.getUserName());

        if(alreadyExists == null)
        {

            // This will hash the password and set it to the user before sending it to the db
            String inputPass = user.getPassword();
            System.out.println("firstpassword:" + inputPass);
            inputPass = HashPassword.hashPassword(inputPass);
            System.out.println("secondpassword:" + inputPass);
            user.setPassword(inputPass);

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.postForEntity("http://localhost:9080/api/postservice/duplicateUser",user, User.class);

            userService.createUser(user);

            loggy.info("The successful creation of a user with username: "+user.getUserName()+".");

            //TODO Redirect to login/frontend
            return new CustomResponseMessage("User was created");
        }
        else {
            loggy.info("The failed creation of a user with username: "+user.getUserName()+".");
            return new CustomResponseMessage("userName already taken");
        }
    }

    /**
     * Api endpoint that returns an Array list of User objects from the service layer.
     * @return Array list of all registered User objects in the HTTP response body.
     */
    @GetMapping(value="/user")
    public List<User> getAllUsers(){
        return userService.getAllUsers();
    }

    //----------------------------------------------------------------------------------------------//

    /**
     * Api endpoint that returns a User object from the service layer.
     * @param userId User identifier (Int).
     * @return User object in the HTTP response.
     */
    @GetMapping(value = "/user/{userId}")
    public User getUserById(@PathVariable("userId") int userId){
        return userService.getUserByID(userId);
    }

    //----------------------------------------------------------------------------------------------//

    /**
     * Api endpoint that updates the User in the application. Receives updated User object
     * and passes the information to the service layer. Returns message when the user is updated
     * in the application via response body.
     * @param user User object.
     * @return Custom response message (string)
     */
    //TODO: might change the session into auth token
    @PutMapping(value = "/user")
    public void updateUser(@RequestHeader("token") String token, @RequestBody User user){
        try{
            // Get the current user from the given token
            // This will throw an error if the token is bad
            JwtEncryption.decrypt(token);

            loggy.info("The successful update(with password) of a user with username: "+user.getUserName()+".");

            //When a user is created it will ping the post service to create a user also
            try {
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.put("http://localhost:9080/api/postservice/updateUser",user, User.class);
            }
            catch (Exception e)
            {
                loggy.error("Post service not reached", e);
            }

            userService.updateUser(user);

        } catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    //----------------------------------------------------------------------------------------------//

    /**
     * Api endpoint that will remove a User from the application. Returns message when the user
     * is deleted through the HTTP response body.
     * @param userId User object.
     * @return Custom response message (string).
     */
    // Christian Kent -- working in postman but must be given ID(in postman)!
    @DeleteMapping(value = "/user/{userId}")
    public void deleteUser(@PathVariable("userId") int userId){

        //When a user is created it will ping the post service to create a user also
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForEntity("http://localhost:9080/api/postservice/deleteUser",userId, Integer.class);

        userService.deleteUser(userId);
        loggy.info("The deletion of a user with user id: "+ userId +".");
    }

    //----------------------------------------------------------------------------------------------//

    /**
     * Api endpoint that sends an email to User's email with a randomly generated password.
     * @param userName Username of User current user (string).
     * @return Custom response message (string).
     */
    //TODO: need to ask team for this kind of implementation, might need to add different service for this

    //----------------------------------------------------------------------------------------------//

    /**
     * Api endpoint that retrieves the logged in user from the HTTP session.
     * @param session HTTP session
     * @return User object
     */
    //TODO: might change the session into auth token
    @GetMapping(value = "/getLoggedInUser")
    public User getLoggedInUser(HttpSession session){
        //try to get the most updated version of the user
        if(session.getAttribute("loggedInUser")!=null){
            int id = ((User)session.getAttribute("loggedInUser")).getUserID();
            User updatedVersion=userService.getUserByID(id);
            session.setAttribute("loggedInUser",updatedVersion);

            loggy.info("The successful retrieval of the loggedInUser");
            return (User) session.getAttribute("loggedInUser");
        }
        else{
            loggy.info("The failed retrieval of the loggedInUser");
            return null;
        }
    }

    //upload profile image (might be in update user)

    //----------------------------------------------------------------------------------------------//

    /**
     * Sends an pre written email to to the email address of the user specified in the param
     * @param user the user who receives the email
     * @return the specified user's email address
     */
    //TODO might need 2 separate 'sendEmail' methods. 1 for email verification & 1 for forgot password
    @GetMapping(value = "/sendEmail")
    public String sendEmail(User user){
        SimpleMailMessage email = new SimpleMailMessage();
        String currUserEmail = user.getEmail();

        email.setTo(currUserEmail);
        email.setSubject("Test subject");
        email.setText("Test body");

        mailSender.send(email);

        return user.getEmail();

    }

    //----------------------------------------------------------------------------------------------//

    /**
     * <p>Sends an email to the user with the username rovided</p>
     * @param username - The username of the user to send an email to
     * @return A string containing a message as to whether or not the username was found in the database.
     */
    @PostMapping("/resetPassword")
    public String resetPassword(String username){

        SimpleMailMessage message = new SimpleMailMessage();
        String emailAddress = "";
        String success = "Email sent.";
        String failure = "Username not found, try again.";

        try{
            emailAddress = userService.getUserByUserName(username).getEmail();
        } catch(NullPointerException e){
            e.printStackTrace();
            loggy.error("User attempted password reset, but no user with username: " + username + " was found.");
            return failure;
        }
        message.setTo(emailAddress);
        message.setSubject("Password Reset");
        message.setText("A request was made to reset the password for your Link account " +
                username + ". If you did not send a request, please disregard this email. Otherwise," +
                "follow the lik below to be redirected to the reset password page. \n");
        mailSender.send(message);

        return success;
    }

    //----------------------------------------------------------------------------------------------//

    /**
     * Method called when a user is attempting to update their password from their profile
     * Checks if the given password is the current user's password
     * @author Brandon, Devin, Loutfi, Joe
     *
     * @param user the current user
     * @return true if good, false if not
     */
    @PostMapping(value="/validate-password")
    public boolean validateOldPassword(@RequestBody User user)
    {
        // We take the incoming password and check if it's the right one
        // First we hash it
        String incomingPassword = user.getPassword();
        incomingPassword = HashPassword.hashPassword(incomingPassword);

        // Then we get the current user from the db
        User current = userService.getUserByUserName(user.getUserName());

        // Return if the hashed passwords match or not
        return current.getPassword().equals(incomingPassword);
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
        User user = userService.getUserByID(JwtEncryption.decrypt(token).getUserID());
        System.out.println("this is user " + user);
        return user;

    }

    //----------------------------------------------------------------------------------------------//

    public UserController() {
    }

    @Autowired
    public UserController(UserService userService, JavaMailSender mailSender) {
        this.userService = userService;
        this.mailSender = mailSender;
    }
}