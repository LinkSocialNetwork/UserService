package com.link.service;

import com.link.dao.FollowDao;
import com.link.model.Follow;
import com.link.model.User;
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
class FollowServiceImplTest {
    @Mock
    FollowDao followDao;

    FollowService followService;

    @BeforeEach
    void setUp() {
        followService = new FollowServiceImpl(followDao);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void insertFollow() {
        User user1 = new User(1,"yaboikev","Kevin","Childs","fluffybunny",new Date(),"kchilds2020@email.com","some bio","/profile","abusinessname",new Date(), "authToken");
        User user2 = new User(2,"yaboikev2","Kevin2","Childs2","fluffybunny2",new Date(),"kchilds20202@email.com","some bio2","/profile2","abusines2sname",new Date(), "authToken");

        Follow follow = new Follow(1, user1, user2);

        followService.insertFollow(follow);

        //Mockito.doNothing().when(followDao.save(follow));

        Mockito.verify(followDao).save(follow);

    }

    @Test
    void deleteAllByFollowerUserIDAndFolloweeUserID() {
        User user1 = new User(1,"yaboikev","Kevin","Childs","fluffybunny",new Date(),"kchilds2020@email.com","some bio","/profile","abusinessname",new Date(), "authToken");
        User user2 = new User(2,"yaboikev2","Kevin2","Childs2","fluffybunny2",new Date(),"kchilds20202@email.com","some bio2","/profile2","abusines2sname",new Date(), "authToken");

        Follow follow = new Follow(1, user1, user2);

        followService.deleteFollowByUserID(user1.getUserID(),user2.getUserID());

        Mockito.verify(followDao).deleteAllByFollowerUserIDAndFolloweeUserID(user1.getUserID(),user2.getUserID());
    }

    @Test
    void getAllFollowers() {
        User user1 = new User(1,"yaboikev","Kevin","Childs","fluffybunny",new Date(),"kchilds2020@email.com","some bio","/profile","abusinessname",new Date(), "authToken");
        User user2 = new User(2,"yaboikev2","Kevin2","Childs2","fluffybunny2",new Date(),"kchi2lds2020@email.com","so2e bio","/profi2le","abusin2essname",new Date(), "authToken");

        List<User> userList = new ArrayList<>();
        userList.add(user1);

        Follow follow = new Follow(1, user1, user2);
        List<Follow> followList = new ArrayList<>();
        followList.add(follow);

        Mockito.when(followDao.findByFollowerUserID(user2.getUserID())).thenReturn(followList);

        List<User> actualUserList = followService.getAllFollowers(user2.getUserID());


        Mockito.verify(followDao).findByFollowerUserID(user2.getUserID());

        assertEquals(userList, actualUserList);
    }

    @Test
    void getAllFollowees() {
        User user1 = new User(1,"yaboikev","Kevin","Childs","fluffybunny",new Date(),"kchilds2020@email.com","some bio","/profile","abusinessname",new Date(), "authToken");
        User user2 = new User(2,"yaboikev2","Kevin2","Childs2","fluffybunny2",new Date(),"kchi2lds2020@email.com","so2e bio","/profi2le","abusin2essname",new Date(), "authToken");
        User user3 = new User(3,"yaboikev23","Kevi3n2","Chi3ds2","fluff3bunny2",new Date(),"kc3hi2lds2020@email.com","so2e b3io","/profi2le","abusin2essname",new Date(), "authToken");

        List<User> userList = new ArrayList<>();
        userList.add(user2);
        userList.add(user3);

        Follow follow = new Follow(1, user1, user2);
        Follow follow1 = new Follow(1, user1, user3);
        List<Follow> followList = new ArrayList<>();
        followList.add(follow);
        followList.add(follow1);

        Mockito.when(followDao.findByFolloweeUserID(user1.getUserID())).thenReturn(followList);

        List<User> actualUserList = followService.getAllFollowees(user1.getUserID());


        Mockito.verify(followDao).findByFolloweeUserID(user1.getUserID());

        assertEquals(userList, actualUserList);

    }
}