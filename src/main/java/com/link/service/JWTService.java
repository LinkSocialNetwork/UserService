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
 * @author Brandon Dcruz - @cptnbrando
 */
public interface JWTService {
    /**
     * This is our HMAC256 algorithm for all tokens
     * Uses an environment variable for a super secret code
     * To verify, we only need the algorithm, claims are irrelevant (they just change the body and make it unique)
     */
    Algorithm algorithm = Algorithm.HMAC256(System.getenv("LINK_TOKEN_SECRET"));

    String generateToken(String username);
    String generateToken(User user);
    Boolean checkToken(String token);
}
