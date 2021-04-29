package com.link.util;

import com.link.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.io.UnsupportedEncodingException;
import java.time.ZonedDateTime;
import java.util.Date;

public class JwtEncryption {
    /**
     * This method will encrypt a user object into a JWT auth token. This token will need to be in request headers from the client
     * @param user full user object
     * */
    public static String encrypt(User user) throws UnsupportedEncodingException {

        /*
        *
        * token expires in 7 days
        * add claim for each property required
        *
        * NOTE these claims can be modified as needed
        * */
        return Jwts.builder()
                //username as subject
                .setSubject(user.getUserName())
                //expires in 7 days
                .setExpiration(Date.from(ZonedDateTime.now().plusDays(7).toInstant()))
                //we can add or remove fields as needed
                .claim("username", user.getUserName())
                .claim("firstname", user.getFirstName())
                .claim("lastname", user.getLastName())
                //sign with environment variable
                .signWith(
                        SignatureAlgorithm.HS256,
                        System.getenv("JWT_SECRET").getBytes("UTF-8")
                )
                .compact();


    }

    /**
     * This method will decrypt a JWT auth token into a user object.
     * @param token encrypted JWT token
     * */
    public static User decrypt(String token) throws UnsupportedEncodingException {

        /*
         *
         * token expires in 7 days
         * get all claims in token and add them to user object to return to frontend
         *
         * */
        Jws<Claims> claims = Jwts.parser()
                .setSigningKey(System.getenv("JWT_SECRET").getBytes("UTF-8"))
                .parseClaimsJws(token);
        //convert all claims to Strings to put into user
        String username = (String) claims.getBody().get("username");
        String firstname = (String) claims.getBody().get("firstname");
        String lastname = (String) claims.getBody().get("lastname");

        User user = new User();
        //can add or remove fields as needed
        user.setUserName(username);
        user.setFirstName(firstname);
        user.setLastName(lastname);
        return user;
    }


}
