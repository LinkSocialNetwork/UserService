package com.link.service;

import com.link.dao.FollowDao;
import com.link.model.Follow;
import com.link.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FollowServiceImpl implements FollowService{

    private FollowDao followDao;

    @Autowired
    public FollowServiceImpl(FollowDao followDao) {
        this.followDao = followDao;
    }

    /**
     * save a new follow pair
     * @param follow a new follow
     */

    @Override
    public void insertFollow(Follow follow) {
        followDao.save(follow);
    }

    /**
     * unfollow people
     * @param follow the targeted follow entry to delete
     */
    @Override
    public void deleteFollow(Follow follow) {
        followDao.delete(follow);
    }

    /**
     * get all the followees as users, will find Followers and get Followees from the list
     * @param userID the user's ID
     * @return Converts a follow list to a user list
     */
    @Override
    public List<User> getAllFollowees(int userID) {
        List<Follow> followList = followDao.findByFollowerUserID(userID);
        List<User> userList = new ArrayList<>();
        for (Follow f : followList) {
            userList.add(f.getFollowee());
        }
        return userList;
    }

    /**
     * get all the followers as users, will find Followees and get Followers from the list
     * @param userID the user's ID
     * @return Converts a follow list to a user list
     */

    @Override
    public List<User> getAllFollowers(int userID) {
        List<Follow> followList = followDao.findByFolloweeUserID(userID);
        List<User> userList = new ArrayList<>();
        for (Follow f : followList) {
            userList.add(f.getFollower());
        }
        return userList;
    }
}
