package com.link.service;

import com.link.model.Follow;
import com.link.model.User;

import java.util.List;

public interface FollowService {
    public void insertFollow(Follow follow);
    public List<User> getAllFollowers(int userID);
    public List<User> getAllFollowees(int userID);
    void deleteFollowByUserID(int followerID, int followeeID);
}
