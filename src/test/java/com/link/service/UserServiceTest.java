package com.link.service;


import com.link.dao.UserDao;
import com.link.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


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

    @Test
    void deleteUser(){
        List<User> following = new ArrayList<>();
        List<User> myFollowing= new ArrayList<>();
        User aUser = new User(1,"yaboikev","Kevin","Childs","fluffybunny",new Date(),"kchilds2020@email.com","some bio","/profile","abusinessname",new Date(), "authToken", following, myFollowing);
        User aNullPost = null;
        userService.deleteUser(aUser.getUserID());
        Mockito.verify(userDao).deleteById(aUser.getUserID());
        aUser = userService.getUserByID(aUser.getUserID());
        assertEquals(aNullPost, aUser);


    }
    @Test
    void getAllUsers() {
        List<User> following = new ArrayList<>();
        List<User> myFollowing= new ArrayList<>();
        List<User> givenList = new ArrayList<>();
        givenList.add(new User (1,"yaboikev","Kevin","Childs","fluffybunny",new Date(),"kchilds2020@email.com","some bio","/profile","abusinessname",new Date(), "authToken", following, myFollowing));
        givenList.add(new User (2,"yaboicorey","Corey","Schink","fluffybunny",new Date(),"cschink2020@email.com","some bio","/profile","abusinessname",new Date(), "authToken", following, myFollowing));
        givenList.add(new User (3,"yaboichristian","Christian","Kent","fluffybunny",new Date(),"ckent2020@email.com","some bio","/profile","abusinessname",new Date(), "authToken", following, myFollowing));
        Mockito.when(userDao.findAll()).thenReturn(givenList);
        List<User> actualReturn = userService.getAllUsers();
        Mockito.verify(userDao).findAll();
        assertEquals(givenList,actualReturn);
    }


}
