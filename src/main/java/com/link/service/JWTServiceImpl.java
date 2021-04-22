package com.link.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.link.model.User;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Implementation of JWTService
 * Contains overloaded methods to use full User objects instead of just the username
 *
 * @author Brandon Dcruz - @cptnbrando
 */
@Service("JWTService")
public class JWTServiceImpl implements JWTService {

    /**
     * Generate a new JWT with a claim of the given user's username
     * Sets it to expire in 1 hour
     * TODO allow generation with longer expiry dates if specified
     *
     * @param username the user's username to generate a token for
     * @return a String JWT, unique, which expires in 1 hour
     */
    @Override
    public String generateToken(String username) {
        String token = "";

        // Creates a new JWT token
        //
        // withClaim attaches the username into the JWT body, making it unique (we can add more to increase uniqueness)
        // withExpiresAt sets it to expire one hour after the current time
        // sign uses the algorithm in the interface with our super secret password
        //
        // Verification does NOT need the claim info, just the algorithm
        try {
            token = JWT.create().withClaim("currentUser", username)
                    .withExpiresAt(new Date(System.currentTimeMillis() + 3600000))
                    .sign(algorithm);
        } catch (JWTCreationException exception){
            //Invalid Signing configuration / Couldn't convert Claims.
            System.out.println("Error generating token!");
            System.out.println(exception);
        }

        return token;
    }

    /**
     * Overloaded method to use full User object
     * Uses generateToken(String username)
     *
     * @param user the user to generate a token for
     * @return a String JWT, unique, which expires in 1 hour
     */
    @Override
    public String generateToken(User user)
    {
        return generateToken(user.getUserName());
    }

    /**
     * Check if a given token is valid or expired
     *
     * Routes to generateToken if the token has expired and the user is still logged in
     * Routes to login if the token has expired / is invalid
     *
     * @param token the token to check
     * @return if the token is good or bad
     */
    @Override
    public Boolean checkToken(String token)
    {
        try {
            // Create the verifier, using the algorithm in the interface
            JWTVerifier verifier = JWT.require(algorithm).build(); //Reusable verifier instance

            // Use the verifier to check the token
            // Will throw an Exception if verification does not pass
            DecodedJWT decoded = verifier.verify(token);

            System.out.println("Verified!");
            return true;
        }
        catch (JWTVerificationException exception)
        {
            System.out.println("Bad token! (Expired or bad input)");
            return false;
        }
    }
}
