package com.link.service;

import com.auth0.jwt.algorithms.Algorithm;
import com.link.model.User;

/**
 * Interface for a service for JSON Web Tokens
 *
 * - Get a new web token (with 1 hour expiry date)
 * - Refresh a web token
 * - Check for a valid web token
 */
public interface JWTService {
    /**
     * This is our HMAC256 algorithm for all tokens
     * Uses an environment variable for a super secret code
     * To verify, we only need the algorithm, claims are irrelevant
     */
    static Algorithm algorithm = Algorithm.HMAC256(System.getenv("LINK_TOKEN_SECRET"));

    /**
     * Generate a new JWT with a claim of the given user's username
     * Sets it to expire in 1 hour
     *
     * @param user the user to generate a token for
     * @return a String JWT, unique, which expires in 1 hour
     */
    String generateToken(User user);

    /**
     * Check if a given token is valid or expired
     * Generates
     *
     * @param token
     * @return
     */
    Boolean checkToken(String token);
}
