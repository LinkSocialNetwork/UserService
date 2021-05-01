package com.link.controllers;

import com.link.dao.FollowDao;
import com.link.model.Follow;
import com.link.model.User;
import com.link.service.FollowService;
import com.link.service.FollowServiceImpl;
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
    @PostMapping("/follow")
    public void addFollow (@RequestBody Follow follow) {
        followService.insertFollow(follow);
    }

    /**
     * delete a follow
     * @param follow the pair to be deleted
     */
    @DeleteMapping("/follow")
    public void deleteFollow (@RequestBody Follow follow) {
        followService.deleteFollow(follow);
    }
}
