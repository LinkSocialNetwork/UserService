package com.link.controllers;

import com.link.model.Follow;
import com.link.model.User;
import com.link.service.FollowService;
import org.junit.jupiter.api.AfterEach;
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

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class FollowControllerTest {
    @Mock
    FollowService followService;

    FollowController followController;

    @BeforeEach
    void setUp() {
        followController = new FollowController(followService);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getAllFollowers() {
        User user1 = new User(1,"yaboikev","Kevin","Childs","fluffybunny",new Date(),"kchilds2020@email.com","some bio","/profile","abusinessname",new Date(), "authToken");
        User user2 = new User(2,"yaboikev2","Kevin2","Childs2","fluffybunny2",new Date(),"kchi2lds2020@email.com","so2e bio","/profi2le","abusin2essname",new Date(), "authToken");
        List<User> userList = new ArrayList<>();
        userList.add(user1);

        Mockito.when(followService.getAllFollowers(user2.getUserID())).thenReturn(userList);

        List<User> actualList = followController.getAllFollowers(user2.getUserID());

        Mockito.verify(followService).getAllFollowers(user2.getUserID());

        assertEquals(userList, actualList);

    }

    @Test
    void getAllFollowees() {
        User user1 = new User(1,"yaboikev","Kevin","Childs","fluffybunny",new Date(),"kchilds2020@email.com","some bio","/profile","abusinessname",new Date(), "authToken");
        User user2 = new User(2,"yaboikev2","Kevin2","Childs2","fluffybunny2",new Date(),"kchi2lds2020@email.com","so2e bio","/profi2le","abusin2essname",new Date(), "authToken");
        List<User> userList = new ArrayList<>();
        userList.add(user2);

        Mockito.when(followService.getAllFollowees(user1.getUserID())).thenReturn(userList);

        List<User> actualList = followController.getAllFollowees(user1.getUserID());

        Mockito.verify(followService).getAllFollowees(user1.getUserID());

        assertEquals(userList, actualList);
    }

    @Test
    void addFollow() {
        Follow follow = new Follow();
        followController.addFollow(follow);
        Mockito.verify(followService).insertFollow(follow);
    }

    @Test
    void deleteFollow() {
        int userId1 = 1;
        int userId2 = 2;
        Follow follow = new Follow();
        followController.deleteFollow(userId1,userId2);
        Mockito.verify(followService).deleteFollowByUserID(userId1,userId2);
    }
}