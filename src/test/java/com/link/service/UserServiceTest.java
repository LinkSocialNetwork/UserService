package com.link.service;


import com.link.dao.UserDao;
import com.link.model.User;
import org.junit.jupiter.api.AfterEach;


import com.link.dao.UserDao;
import com.link.model.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;


import java.util.Calendar;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;




@SpringBootTest //this will actually start the IoC container and perform your autowiring
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserDao userDao;
    private UserService userService;


    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userDao);

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getUserByID() {
        User newUser=new User();
        newUser.setUserID(1);
        newUser.setUserName("TophTheGreatest");
        newUser.setPassword("melonlord");
        newUser.setBio("Greatest Earthbender to ever live.");
        newUser.setFirstName("Toph");
        newUser.setLastName("Bei-Fong");
        newUser.setAuthToken("BlahBaBlah");
        newUser.setBusinessName("Toph Greatest EarthBender");
        Date myDate;
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, 8);
        cal.set(Calendar.DATE, 24);
        cal.set(Calendar.YEAR, 2000);
        myDate=cal.getTime();
        newUser.setDob(myDate);
        newUser.setEmail("toph@yahoo.com");
        newUser.setProfileImg("d");
        newUser.setDateCreated(new Date());

        Mockito.when(userDao.findById(1)).thenReturn(newUser);

        User rUser = userService.getUserByID(1);
        Mockito.verify(userDao).findById(1);
        assertEquals(newUser,rUser);
    }

    @Test
    void createUser(){
        User temp = new User();
        userService.createUser(temp);
        Mockito.verify(userDao).save(temp);
    }
    @Test
    void updateUser(){
        User temp = new User();
        temp.setUserName("dang");
        temp.setUserID(1);
        Mockito.when(userDao.save(temp)).thenReturn(temp);
        Mockito.when(userDao.findByUserName(temp.getUserName())).thenReturn(temp);
         userService.updateUser(temp);
         User actualUser = userService.getUserByUserName(temp.getUserName());
         Mockito.verify(userDao).save(temp);
         assertEquals(temp, actualUser);

    }
    @Test
    void logIn(){
        User temp = new User();
        temp.setUserName("dang");
        temp.setPassword("pass");
        temp.setUserID(1);
        Mockito.when(userDao.findByUserName(temp.getUserName())).thenReturn(temp);
        User actualUser = userService.getUserByUserName(temp.getUserName());
        Mockito.verify(userDao).findByUserName(temp.getUserName());
        assertEquals(temp, actualUser);


    }



    @Test
    void deleteUser(){
        List<User> following = new ArrayList<>();
        List<User> myFollowing= new ArrayList<>();
        User aUser = new User(1,"yaboikev","Kevin","Childs","fluffybunny",new Date(),"kchilds2020@email.com","some bio","/profile","abusinessname",new Date(), "authToken");
        User aNullPost = null;
        userService.deleteUser(aUser.getUserID());
        Mockito.verify(userDao).deleteById(aUser.getUserID());
        aUser = userService.getUserByID(aUser.getUserID());
        assertEquals(aNullPost, aUser);


    }
    @Test
    void getUserByUserName(){
        User expectedUser = new User();
        expectedUser.setUserName("Christian");
        expectedUser.setUserID(1);
        Mockito.when(userDao.findByUserName(expectedUser.getUserName())).thenReturn(expectedUser);
        User actualUser = userService.getUserByUserName(expectedUser.getUserName());
        Mockito.verify(userDao).findByUserName(expectedUser.getUserName());
        assertEquals(expectedUser, actualUser);


    }
    @Test
    void getNullUserByUserName(){
            User nullUser = new User();
           Mockito.when(userService.getUserByUserName(nullUser.getUserName())).thenReturn(nullUser);
           User actualNullUser = userService.getUserByUserName(nullUser.getUserName());
           assertEquals(nullUser, actualNullUser);
    }
    @Test
    void getAllUsers() {
        List<User> following = new ArrayList<>();
        List<User> myFollowing= new ArrayList<>();
        List<User> givenList = new ArrayList<>();
        givenList.add(new User (1,"yaboikev","Kevin","Childs","fluffybunny",new Date(),"kchilds2020@email.com","some bio","/profile","abusinessname",new Date(), "authToken"));
        givenList.add(new User (2,"yaboicorey","Corey","Schink","fluffybunny",new Date(),"cschink2020@email.com","some bio","/profile","abusinessname",new Date(), "authToken"));
        givenList.add(new User (3,"yaboichristian","Christian","Kent","fluffybunny",new Date(),"ckent2020@email.com","some bio","/profile","abusinessname",new Date(), "authToken"));
        Mockito.when(userDao.findAll()).thenReturn(givenList);
        List<User> actualReturn = userService.getAllUsers();
        Mockito.verify(userDao).findAll();
        assertEquals(givenList,actualReturn);
    }


}


