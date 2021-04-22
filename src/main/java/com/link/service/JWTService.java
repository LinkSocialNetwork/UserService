package com.link.service;

import com.auth0.jwt.algorithms.Algorithm;
import com.link.model.User;

/**
 * Interface for a service for JSON Web Tokens
 *
 * - Get a new web token (with 1 hour expiry date)
 * - TODO ability to set a longer expiry date if the user wants one (like a "stay logged in" button on the login page)
 * - Check for a valid web token
 *
 * @author Brandon Dcruz
 */
public interface JWTService {
    /**
     * This is our HMAC256 algorithm for all tokens
     * Uses an environment variable for a super secret code
     * To verify, we only need the algorithm, claims are irrelevant (they just change the body and make it unique)
     */
    static Algorithm algorithm = Algorithm.HMAC256(System.getenv("LINK_TOKEN_SECRET"));

    /**
     * Generate a new JWT with a claim of the given user's username
     * Sets it to expire in 1 hour
     *
     * @param username the user's username to generate a token for
     * @return a String JWT, unique, which expires in 1 hour
     */
    String generateToken(String username);

    /**
     * Overloaded method to use full User object
     * Uses generateToken(String username)
     *
     * @param user the user to generate a token for
     * @return a String JWT, unique, which expires in 1 hour
     */
    String generateToken(User user);

    /**
     * Check if a given token is valid or expired
     *
     * Routes to generateToken if the token has expired and the user is still logged in
     * Routes to login if the token has expired / is invalid
     *
     * @param token the token to check
     * @return if the token is good or bad
     */
    Boolean checkToken(String token);
}
