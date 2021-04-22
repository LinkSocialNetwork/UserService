package com.link.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.link.model.User;

import java.util.Date;

public class JWTServiceImpl implements JWTService {

    @Override
    public String generateToken(User user)
    {
        String token = "";

        try {
            // Creates a new JWT token
            //
            // withClaim attaches the username into the JWT body, making it unique (we can add more to increase uniqueness)
            // withExpiresAt sets it to expire one hour after the current time
            // sign uses the algorithm in the interface with our super secret password
            //
            // Verification does NOT need the claim info, just the algorithm
            token = JWT.create().withClaim("currentUser", user.getUserName())
                    .withExpiresAt(new Date(System.currentTimeMillis() + 3600000))
                    .sign(algorithm);
        } catch (JWTCreationException exception){
            //Invalid Signing configuration / Couldn't convert Claims.
            System.out.println("Error generating token!");
            System.out.println(exception);
        }

        return token;
    }

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

            //TODO if token expired but user is still using the site, generate a new token
            return false;
        }
    }
}
