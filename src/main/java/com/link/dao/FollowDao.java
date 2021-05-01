package com.link.dao;

import com.link.model.Follow;
import com.link.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Get list of followers and followees
 */
@Transactional
public interface FollowDao extends JpaRepository<Follow, Integer> {
    /**
     * finds followers by userIDs
     * @param userID the user's id
     * @return list of followers
     */
    public List<Follow> findByFollowerUserID(int userID);

    /**
     * finds followees by userIDs
     * @param userID the user's id
     * @return list of followees
     */
    public List<Follow> findByFolloweeUserID(int userID);

    /**
     * finds the entry with the two users and deletes
     * @param followerUserID the follower's id
     * @param followeeUserID the followee's id
     */
    public void deleteAllByFollowerUserIDAndFolloweeUserID(int followerUserID, int followeeUserID);

}
