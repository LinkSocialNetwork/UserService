package com.link.dao;

import com.link.model.Follow;
import com.link.model.User;
import com.netflix.discovery.converters.Auto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class FollowDaoTest {

    @Autowired
    UserDao userDao;

    @Autowired
    FollowDao followDao;

    @BeforeEach
    void setUp() {


    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void findByFollowerUserID() {
        User user1 = userDao.findById(1);
        User user2 = userDao.findById(2);
        User user3 = userDao.findById(3);
        User user4 = userDao.findById(4);
        User user5 = userDao.findById(5);

        Follow follow1 = new Follow(1, user1, user2);
        Follow follow2 = new Follow(2, user1, user3);
        Follow follow3 = new Follow(3, user1, user4);
        Follow follow4 = new Follow(4, user1, user5);

        followDao.save(follow1);
        followDao.save(follow2);
        followDao.save(follow3);
        followDao.save(follow4);

        List<Follow> expectedUsers = new ArrayList<>();
        expectedUsers.add(follow1);
        expectedUsers.add(follow2);
        expectedUsers.add(follow3);
        expectedUsers.add(follow4);
        List<Follow> actualUsers = followDao.findByFollowerUserID(1);

        assertEquals(expectedUsers,actualUsers);
    }

    @Test
    void findByFolloweeUserID() {
        User user1 = userDao.findById(1);
        User user2 = userDao.findById(2);
        User user3 = userDao.findById(3);
        User user4 = userDao.findById(4);
        User user5 = userDao.findById(5);

        Follow follow1 = new Follow(1, user1, user2);
        Follow follow2 = new Follow(2, user1, user3);
        Follow follow3 = new Follow(3, user1, user4);
        Follow follow4 = new Follow(4, user1, user5);

        followDao.save(follow1);
        followDao.save(follow2);
        followDao.save(follow3);
        followDao.save(follow4);

        List<Follow> expectedUsers = new ArrayList<>();
//        expectedUsers.add(follow1);
        expectedUsers.add(follow2);
//        expectedUsers.add(follow3);
//        expectedUsers.add(follow4);
        List<Follow> actualUsers = followDao.findByFolloweeUserID(3);

        assertEquals(expectedUsers,actualUsers);
    }

    @Test
    void deleteAllByFollowerUserIDAndFolloweeUserID() {
        User user1 = userDao.findById(1);
        User user2 = userDao.findById(2);
        User user3 = userDao.findById(3);
        User user4 = userDao.findById(4);
        User user5 = userDao.findById(5);

        Follow follow1 = new Follow(1, user1, user2);
        Follow follow2 = new Follow(2, user1, user3);
        Follow follow3 = new Follow(3, user1, user4);
        Follow follow4 = new Follow(4, user1, user5);

        followDao.save(follow1);
        followDao.save(follow2);
        followDao.save(follow3);
        followDao.save(follow4);

        List<Follow> expectedUsers = new ArrayList<>();
        expectedUsers.add(follow1);
//        expectedUsers.add(follow2);
        expectedUsers.add(follow3);
        expectedUsers.add(follow4);

        followDao.deleteAllByFollowerUserIDAndFolloweeUserID(1,3);

        List<Follow> actualUsers = followDao.findByFollowerUserID(1);

        assertEquals(expectedUsers, actualUsers);
    }

}