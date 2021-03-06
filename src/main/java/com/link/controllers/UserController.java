package com.link.controllers;


import com.link.model.CustomResponseMessage;
import com.link.model.User;
import com.link.service.UserService;
import com.link.util.HashPassword;
import com.link.util.JwtEncryption;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

import javax.ws.rs.Path;
import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.web.client.RestTemplate;


@RestController
@RequestMapping("/api/userservice")
@Getter
@Setter
public class UserController {

    private JavaMailSender mailSender;
    private UserService userService;

    //rest template
    private RestTemplate restTemplate= new RestTemplate();

    final static Logger loggy = Logger.getLogger(UserController.class);
    static {
        loggy.setLevel(Level.ALL);
    }

    public UserController() {
    }

    @Autowired
    public UserController(UserService userService, JavaMailSender mailSender) {
        this.userService = userService;
        this.mailSender = mailSender;
    }

    public UserController(UserService userService) {
        this.userService = userService;
    }



    /**
     * Api endpoint that inserts User object into the application depending on whether they exists or not.
     * @param user User object.
     * @return Custom response message (string).
     */
    @PostMapping(value = "/user")
    public CustomResponseMessage insertNewUser(@RequestBody User user){

        User usernameExists = userService.getUserByUserName(user.getUserName().toLowerCase());
        User emailExists = userService.getUserByEmail(user.getEmail().toLowerCase());

        if(usernameExists != null)
            return new CustomResponseMessage("Username already exists in system");

        if(emailExists != null)
            return new CustomResponseMessage("email already exists in system");

        user.setUserName(user.getUserName().toLowerCase());
        user.setEmail(user.getEmail().toLowerCase());
        // This will hash the password and set it to the user before sending it to the db
        String inputPass = user.getPassword();
        inputPass = HashPassword.hashPassword(inputPass);
        user.setPassword(inputPass);

        //if rest template was unsuccessful in creating a user in db, then return "Could not create user"
        try {
            User postServiceUser = restTemplate.postForEntity("http://localhost:9080/api/postservice/duplicateUser",
                    user, User.class).getBody();

            //make sure ids are the same
            user.setUserID(postServiceUser.getUserID());
            userService.createUser(user);

            loggy.info("The successful creation of a user with username: " + user.getUserName() + ".");
        }catch(Exception e){
            e.printStackTrace();
            return new CustomResponseMessage("Could not create user");
        }

        return new CustomResponseMessage("User was created");


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
    @PutMapping(value = "/protected/user")
    public boolean updateUser(@RequestHeader("token") String token, @RequestBody User user){
        //get user object
        User userFromDb = userService.getUserByID(user.getUserID());

        try{
            JwtEncryption.decrypt(token);

            //check if username has been changed
            if(!userFromDb.getUserName().equalsIgnoreCase(user.getUserName())){
                //check if new username is already taken
                User taken = userService.getUserByUserName(user.getUserName().toLowerCase());

                if(taken != null)
                    return false;
            }

            //check if email has been changed
            if(!userFromDb.getEmail().equalsIgnoreCase(user.getEmail())){
                //check if new email is already taken
                User taken = userService.getUserByEmail(user.getEmail().toLowerCase());

                if(taken != null)
                    return false;
            }

            restTemplate.put("http://localhost:9080/api/postservice/updateUser",user, User.class);

            userService.updateUser(user);
            return true;

        } catch(Exception e)
        {
            e.printStackTrace();
            return false;
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
    @Deprecated
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

        String currUserEmail = "saimon.91@hotmail.com";

        email.setTo(currUserEmail);
        email.setSubject("Test subject");
        email.setText("Test body");

        mailSender.send(email);

        return user.getEmail();

    }

    //----------------------------------------------------------------------------------------------//

    /**
     * <p>Sends an email to the user with the username provided</p>
     * @param username - The username of the user to send an email to
     * @return A string containing a message as to whether or not the username was found in the database.
     */
    //TODO: may be depricated. need to make tests in we are not depricating
    @PostMapping("/resetPassword")
    public CustomResponseMessage resetPassword(@RequestBody String username){

        SimpleMailMessage message = new SimpleMailMessage();
        String emailAddress;
        String success = "Email sent.";
        String failure = "Username not found, try again.";
        User user = userService.getUserByUserName(username);
        if(user == null){
            System.out.println("no user");
            loggy.error("User attempted password reset, but no user with username: " + username + " was found.");
            return new CustomResponseMessage("no user with this user name");

        }
        emailAddress = user.getEmail();

        String newPass=HashPassword.generateTempPassword(10) ;
        message.setTo(emailAddress);
        message.setSubject("Password Reset Request");
        message.setText("A request was made to reset the password for your Link account (" +
                username + "). If you did not send a request, please disregard this email. Otherwise," +
                "this is your new password. \n ("+ newPass+")");
        User tempUser = userService.getUserByUserName(username);
        tempUser.setPassword(HashPassword.hashPassword(newPass));
        tempUser.setCheckPassword(1);
        mailSender.send(message);
        userService.updateUser(tempUser);


        return new CustomResponseMessage(success);
    }



    //----------------------------------------------------------------------------------------------//



    /**
     * <p>Sends an email to the user to verify his email</p>
     * @param user - its a container with code to send and user name to retrieve user email
     * @return A CustomResponseMessage containing a message that the email was sent and after that check the code to verify the email.
     */

    @PostMapping("/protected/verify-email")
    public CustomResponseMessage verifyEmail(@RequestBody User user){

        User tempUser = userService.getUserByUserName(user.getUserName());
        SimpleMailMessage message = new SimpleMailMessage();
        String emailAddress;
        if(user.getLastName().equals("sendEmail")){

            emailAddress = tempUser.getEmail();
            message.setTo(emailAddress);
            message.setSubject("Link Email Verification");
            message.setText("Hello "+tempUser.getFirstName()+" "+tempUser.getLastName()+"\n" +
                    "\n" +
                    "You registered an account on Link, you need to verify that this is your email address using this code: ("+user.getFirstName()+")\n" +
                    "\n" +
                    "Kind Regards, Link \n");

            mailSender.send(message);
            loggy.info("Code sent to verify the email");
            return new CustomResponseMessage("sent");
        }else{
            if(user.getFirstName().equals(user.getLastName())){
                tempUser.setCheckEmail(0);
                userService.updateUser(tempUser);
                loggy.info("User Verified his Email.");
                return new CustomResponseMessage("email Verified");
            }else{
                return new CustomResponseMessage("wrong code");
            }
        }

    }



    //----------------------------------------------------------------------------------------------//

    /**
     * Method called when a user is attempting to update their password from their profile
     * Checks if the given password is the current user's password
     * @author Brandon, Devin, Loutfi, Joe
     *
     * @param username username of user in db
     * @param oldPassword password to verify in db
     * @param newPassword new password to be set
     * @return true if good, false if not
     */
    @PostMapping(value="/protected/validate-password")
    public boolean updatePassword(@RequestParam("username") String username, @RequestParam("oldpassword") String oldPassword, @RequestParam("newpassword") String newPassword)
    {
        // We take the incoming password and check if it's the right one
        // First we hash it
        String incomingPassword = HashPassword.hashPassword(oldPassword);


        // Then we get the current user from the db
        User current = userService.getUserByUserName(username);

        // return false if password does not match
        if(!current.getPassword().equals(incomingPassword))
            return false;

        current.setCheckPassword(0);
        current.setPassword(HashPassword.hashPassword(newPassword));
        //password needs to be updated
        this.userService.updateUser(current);
        return true;
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

        User newUser = userService.getUserByUserName(user.getUserName().toLowerCase());

        if (newUser == null)
        {
            loggy.info("Login: can't find username! Received: " + user.getUserName().toLowerCase());
            return null;
        }

        String entered = user.getPassword();
        if(HashPassword.hashPassword(entered).equals(newUser.getPassword())) {
            newUser.setAuthToken(JwtEncryption.encrypt(newUser));
            return newUser;
        }
        else {
            loggy.info("Login: can't authenticate password! Received: " + user.getUserName().toLowerCase());
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
    public User checkToken(@RequestHeader("token") String token) throws UnsupportedEncodingException {
        if(token == null)
        {
            //return jwtService.checkToken(user.getAuthToken());
            return null;
        }
        User user = userService.getUserByID(JwtEncryption.decrypt(token).getUserID());
        return user;

    }

    //----------------------------------------------------------------------------------------------//


}