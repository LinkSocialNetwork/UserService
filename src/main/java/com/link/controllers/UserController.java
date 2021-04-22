package com.link.controllers;

import com.link.model.User;
import com.link.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;



@RestController
@CrossOrigin
@RequestMapping("/users")
public class UserController {
    UserController userController;
    private UserService userService;
    private PasswordEncoder passwordEncoder;
    final static Logger loggy = Logger.getLogger(UserController.class);
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
    //TODO: need to figure out how to use auth token for log-in
    @PostMapping("/login")
    public User logIn(HttpSession session,@RequestBody User user){
        /*
        since the password is now encrypted the new user must be retrieved by
        its username instead of by a dummy user object, as it was pre pw hashing.
        */
        User newUser= userService.getUserByUserName(user.getUserName());

        System.out.println("userInfo: "+user.getPassword());
        System.out.println("newUser: "+newUser.getPassword());

        //check if the PW input on client side matches the encrypted PW in our DB
        if(passwordEncoder.matches(user.getPassword(),newUser.getPassword())){
            System.out.println("they match");
            //same login logic as before password encryption
            session.setAttribute("loggedInUser", newUser);

            User currentUser = (User) session.getAttribute("loggedInUser");

            return newUser;

        }else return new User();        // should fix this that return empty user
    }

    /**
     * Api endpoint that inserts User object into the application depending on whether they exists or not.
     * @param user User object.
     * @return Custom response message (string).
     */
    @PostMapping(value = "/insertNewUser")
    public void insertNewUser(@RequestBody User user){
        User alreadyExists = getUserByUsername(user);
        if(alreadyExists == null) {
            userService.createUser(user);
            loggy.info("The successful creation of a user with username: "+user.getUserName()+".");

        }
        else {
            loggy.info("The failed creation of a user with username: "+user.getUserName()+".");

        }
    }

    /**
     * Api endpoint that receives User object to use username to retreive User object from service layer.
     * @param user User object from HTTP request.
     * @return User object based on the username.
     */
    @PostMapping(value = "/getByUsername")
    public User getUserByUsername(@RequestBody User user) {
        loggy.info("An attempt to retrieve a user with username: "+user.getUserName()+".");
        return userService.getUserByUserName(user.getUserName());
    }

    /**
     * Api endpoint that returns an Array list of User objects from the service layer.
     * @return Array list of all registered User objects in the HTTP response body.
     */
    @GetMapping(value="getAllUsers")
    public List<User> getAllUsers(){

        return userService.getAllUsers();
    }


    /**
     * Api endpoint that returns a User object from the service layer.
     * @param userId User identifier (Int).
     * @return User object in the HTTP response.
     */
    @GetMapping(value = "/getUserById/{userId}")
    public User getUserById(@PathVariable("userId") int userId){

        return userService.getUserByID(userId);
    }

    /**
     * Api endpoint that updates the User in the application. Receives updated User object
     * and passes the information to the service layer. Returns message when the user is updated
     * in the application via response body.
     * @param session HTTP Session of current logged in user.
     * @param user User object.
     * @return Custom response message (string)
     */
    //TODO: might change the session into auth token
    @PutMapping(value = "/updateUser")
    public void updateUser(HttpSession session,@RequestBody User user){
        if((User)session.getAttribute("loggedInUser")!=null){
            User current=((User)session.getAttribute("loggedInUser"));
            if(!current.getPassword().equals(user.getPassword())){
                userService.updateUser(user);
                loggy.info("The successful update(with password) of a user with username: "+user.getUserName()+".");
            }
            else {
                loggy.info("The successful update of a user with username: "+user.getUserName()+".");
                userService.updateUser(user);
            }
            int id = current.getUserID();
            User updatedVersion=getUserById(id);
            session.setAttribute("loggedInUser",updatedVersion);

        }
        loggy.info("The failed update of a user with username: "+user.getUserName()+".");

    }

    /**
     * Api endpoint that will remove a User from the application. Returns message when the user
     * is deleted through the HTTP response body.
     * @param user User object.
     * @return Custom response message (string).
     */
    @DeleteMapping(value = "/deleteUser")
    public void deleteUser(@RequestBody User user){
        userService.deleteUser(user);
        loggy.info("The deletion of a user with username: "+user.getUserName()+".");

    }

    /**
     * Api endpoint that sends an email to User's email with a randomly generated password.
     * @param userName Username of User current user (string).
     * @return Custom response message (string).
     */
    //TODO: need to ask team for this kind of implementation, might need to add different service for this


    /**
     * Api endpoint that logs out the user from the application. Redirects to landing page.
     * @param myReq HTTP servlet request
     * @return Custom Response message (string)
     */
    //TODO: might change the session into auth token
    @GetMapping(value = "logout")
    public void logout(HttpServletRequest myReq){
        HttpSession userSession = myReq.getSession();
        loggy.info("The successful logout of the session:"+userSession);
        userSession.invalidate();
    }

    /**
     * Api endpoint that retrieves the logged in user from the HTTP session.
     * @param session HTTP session
     * @return User object
     */
    //TODO: might change the session into auth token
    @GetMapping(value = "/getLoggedInUser")
    public User getLoggedInUser(HttpSession session){
        //try to get the most updated version of the user
        if((User)session.getAttribute("loggedInUser")!=null){
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


<<<<<<< HEAD

=======
>>>>>>> f0bc63eda52773db767d832b2d69325e08651ab9


    public UserController() {
    }

    @Autowired
    public UserController(UserController userController, UserService userService, PasswordEncoder passwordEncoder) {
        this.userController = userController;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }
}
