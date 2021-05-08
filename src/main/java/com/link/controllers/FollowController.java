package com.link.controllers;

import com.link.model.Follow;
import com.link.model.User;
import com.link.service.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/userservice")
public class FollowController {

    private FollowService followService;

    @Autowired
    public FollowController(FollowService followService) {
        this.followService = followService;
    }

    /**
     * get all followers as users
     * @param userID the user's ID
     * @return the list of Followers
     */
    @GetMapping("/follow/follower/{userID}")
    public List<User> getAllFollowers (@PathVariable int userID) {
        return followService.getAllFollowers(userID);
    }

    /**
     * get all followees as users
     * @param userID the user's ID
     * @return the list of Followees
     */
    @GetMapping("/follow/followee/{userID}")
    public List<User> getAllFollowees (@PathVariable int userID) {
        return followService.getAllFollowees(userID);
    }

    /**
     * save a new follow
     * @param follow the new pair to be followed
     */
    @PostMapping("/protected/follow")
    public void addFollow (@RequestBody Follow follow) {
        followService.insertFollow(follow);
    }

    /**
     * endpoint to delete a follow
     * @param followerID the follower's id
     * @param followeeID the followee's id
     */
    @DeleteMapping("/protected/follow/{followerID}/{followeeID}")
    public void deleteFollow (@PathVariable int followerID, @PathVariable int followeeID) {
        followService.deleteFollowByUserID(followerID, followeeID);
    }
}
