package com.link.model;



import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jdk.nashorn.internal.ir.annotations.Ignore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

@Entity
//@Data
@Getter
@Setter
@AllArgsConstructor
@ToString
@NoArgsConstructor
@Table(name= "UserAccount")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "user_id")
    private int userID;

    @Column(name= "user_name", unique = true, nullable = false)
    private String userName;

    @Column(name= "first_name", unique = false, nullable = false)
    private String firstName;

    @Column(name= "last_name", unique = false, nullable = false)
    private String lastName;

    @Column(name= "password", unique = false, nullable = false)
    @JsonIgnore
    private String password;

    @Column(name= "dob", unique = false, nullable = false)
    private Date dob;

    @Column(name= "email", unique = true, nullable = false)
    private String email;

    @Column(name= "bio", unique = false, nullable = true)
    private String bio;

    @Column(name= "profile_img_url", unique = false, nullable = false)
    private String profileImg;

    @Column(name= "business_name", unique = false, nullable = true)
    private String businessName;

    @Column(name= "date_created", unique = false, nullable = false)
    private Date dateCreated;

    @Column(name = "authToken", unique = true, nullable = true)
    private String authToken;

    //follows
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinColumn(name= "user_id", nullable = true)
    private List<User> following;

    //may need to add a "people who are following me" column as well
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinColumn(name= "user_id", nullable = true)
    private List<User> myFollowers;
}
