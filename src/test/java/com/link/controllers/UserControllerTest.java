package com.link.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.link.model.CustomResponseMessage;
import com.link.model.User;
import com.link.service.UserService;
import com.link.util.JwtEncryption;
import com.sun.jdi.event.ExceptionEvent;
import jdk.nashorn.internal.ir.debug.JSONWriter;
import jdk.nashorn.internal.parser.JSONParser;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.UncheckedIOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    UserController userController;
    @Mock
    UserService userService;
    @Mock
    RestTemplate restTemplate;
    @Mock
    JwtEncryption jwtEncryption;

    @BeforeEach
    void setUp() {
        userController = new UserController(userService);
        userController.setRestTemplate(this.restTemplate);
        jwtEncryption= new JwtEncryption();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getUserById() {
        User newUser=new User();
        newUser.setUserID(1);
        newUser.setUserName("TophTheGreatest");
        newUser.setPassword("melonlord");
        newUser.setBio("Greatest Earthbender to ever live.");
        newUser.setFirstName("Toph");
        newUser.setLastName("Bei-Fong");
        newUser.setAuthToken("BlahBaBlah");
        newUser.setBusinessName("Toph Greatest EarthBender");
        Date myDate;
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, 8);
        cal.set(Calendar.DATE, 24);
        cal.set(Calendar.YEAR, 2000);
        myDate=cal.getTime();
        newUser.setDob(myDate);
        newUser.setEmail("toph@yahoo.com");
        newUser.setProfileImg("d");
        newUser.setDateCreated(new Date());

        Mockito.when(userService.getUserByID(1)).thenReturn(newUser);

        User rUser = userController.getUserById(1);
        Mockito.verify(userService).getUserByID(1);
        assertEquals(newUser,rUser);
    }

    @Test
    void insertNewUser_ReturnsCRM_WhenUsernameAlreadyExists(){
        User newUser=new User();
        newUser.setUserID(1);
        newUser.setUserName("TophTheGreatest");
        newUser.setPassword("melonlord");
        newUser.setBio("Greatest Earthbender to ever live.");
        newUser.setFirstName("Toph");
        newUser.setLastName("Bei-Fong");
        newUser.setAuthToken("BlahBaBlah");
        newUser.setBusinessName("Toph Greatest EarthBender");
        Date myDate;
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, 8);
        cal.set(Calendar.DATE, 24);
        cal.set(Calendar.YEAR, 2000);
        myDate=cal.getTime();
        newUser.setDob(myDate);
        newUser.setEmail("toph@yahoo.com");
        newUser.setProfileImg("d");
        newUser.setDateCreated(new Date());

        Mockito.when(userService.getUserByUserName("tophthegreatest")).thenReturn(newUser);
        Mockito.when(userService.getUserByEmail("toph@yahoo.com")).thenReturn(null);

        CustomResponseMessage crmActual=  userController.insertNewUser(newUser);
        CustomResponseMessage crmTest= new CustomResponseMessage("Username already exists in system");

        assertEquals(crmTest, crmActual);
    }

    @Test
    void insertNewUser_ReturnsCRM_WhenEmailAlreadyExists(){
        User newUser=new User();
        newUser.setUserID(1);
        newUser.setUserName("TophTheGreatest");
        newUser.setPassword("melonlord");
        newUser.setBio("Greatest Earthbender to ever live.");
        newUser.setFirstName("Toph");
        newUser.setLastName("Bei-Fong");
        newUser.setAuthToken("BlahBaBlah");
        newUser.setBusinessName("Toph Greatest EarthBender");
        Date myDate;
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, 8);
        cal.set(Calendar.DATE, 24);
        cal.set(Calendar.YEAR, 2000);
        myDate=cal.getTime();
        newUser.setDob(myDate);
        newUser.setEmail("toph@yahoo.com");
        newUser.setProfileImg("d");
        newUser.setDateCreated(new Date());

        Mockito.when(userService.getUserByUserName("tophthegreatest")).thenReturn(null);
        Mockito.when(userService.getUserByEmail("toph@yahoo.com")).thenReturn(newUser);

        CustomResponseMessage crmActual=  userController.insertNewUser(newUser);
        CustomResponseMessage crmTest= new CustomResponseMessage("email already exists in system");

        assertEquals(crmTest, crmActual);
    }

    @Test
    void insertNewUser_ReturnsCRM_WhenExceptionInCreateUserRestTemplate(){
        User newUser=new User();
        newUser.setUserID(1);
        newUser.setUserName("TophTheGreatest");
        newUser.setPassword("melonlord");
        newUser.setBio("Greatest Earthbender to ever live.");
        newUser.setFirstName("Toph");
        newUser.setLastName("Bei-Fong");
        newUser.setAuthToken("BlahBaBlah");
        newUser.setBusinessName("Toph Greatest EarthBender");
        Date myDate;
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, 8);
        cal.set(Calendar.DATE, 24);
        cal.set(Calendar.YEAR, 2000);
        myDate=cal.getTime();
        newUser.setDob(myDate);
        newUser.setEmail("toph@yahoo.com");
        newUser.setProfileImg("d");
        newUser.setDateCreated(new Date());

        Mockito.when(userService.getUserByUserName("tophthegreatest")).thenReturn(null);
        Mockito.when(userService.getUserByEmail("toph@yahoo.com")).thenReturn(null);

        Mockito.when(restTemplate.postForEntity("http://localhost:9080/api/postservice/duplicateUser",
                newUser, User.class)).thenThrow(new RuntimeException());

        CustomResponseMessage crmActual=  userController.insertNewUser(newUser);
        CustomResponseMessage crmTest= new CustomResponseMessage("Could not create user");

        assertEquals(crmTest, crmActual);
    }

    @Test
    void insertNewUser_ReturnsCRM_WhenUserCreated(){
        User newUser=new User();
        newUser.setUserID(1);
        newUser.setUserName("TophTheGreatest");
        newUser.setPassword("melonlord");
        newUser.setBio("Greatest Earthbender to ever live.");
        newUser.setFirstName("Toph");
        newUser.setLastName("Bei-Fong");
        newUser.setAuthToken("BlahBaBlah");
        newUser.setBusinessName("Toph Greatest EarthBender");
        Date myDate;
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, 8);
        cal.set(Calendar.DATE, 24);
        cal.set(Calendar.YEAR, 2000);
        myDate=cal.getTime();
        newUser.setDob(myDate);
        newUser.setEmail("toph@yahoo.com");
        newUser.setProfileImg("d");
        newUser.setDateCreated(new Date());

        Mockito.when(userService.getUserByUserName("tophthegreatest")).thenReturn(null);
        Mockito.when(userService.getUserByEmail("toph@yahoo.com")).thenReturn(null);

        Mockito.when(restTemplate.postForEntity("http://localhost:9080/api/postservice/duplicateUser",
                newUser, User.class)).thenReturn(ResponseEntity.of(Optional.of(newUser)));

        CustomResponseMessage crmActual=  userController.insertNewUser(newUser);
        CustomResponseMessage crmTest= new CustomResponseMessage("User was created");

        assertEquals(crmTest, crmActual);
    }

    @Test
    void getAllUsers_ReturnUserList_When2UsersCreated(){
        User newUser=new User();
        newUser.setUserID(1);
        newUser.setUserName("TophTheGreatest");
        newUser.setPassword("melonlord");
        newUser.setBio("Greatest Earthbender to ever live.");
        newUser.setFirstName("Toph");
        newUser.setLastName("Bei-Fong");
        newUser.setAuthToken("BlahBaBlah");
        newUser.setBusinessName("Toph Greatest EarthBender");
        Date myDate;
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, 8);
        cal.set(Calendar.DATE, 24);
        cal.set(Calendar.YEAR, 2000);
        myDate=cal.getTime();
        newUser.setDob(myDate);
        newUser.setEmail("toph@yahoo.com");
        newUser.setProfileImg("d");
        newUser.setDateCreated(new Date());

        User newUser2=new User();
        newUser.setUserID(1);
        newUser.setUserName("TophTheGreatest");
        newUser.setPassword("melonlord");
        newUser.setBio("Greatest Earthbender to ever live.");
        newUser.setFirstName("Toph");
        newUser.setLastName("Bei-Fong");
        newUser.setAuthToken("BlahBaBlah");
        newUser.setBusinessName("Toph Greatest EarthBender");
        cal.set(Calendar.MONTH, 8);
        cal.set(Calendar.DATE, 24);
        cal.set(Calendar.YEAR, 2000);
        myDate=cal.getTime();
        newUser.setDob(myDate);
        newUser.setEmail("toph@yahoo.com");
        newUser.setProfileImg("d");
        newUser.setDateCreated(new Date());

        List<User> userList= new ArrayList();
        userList.add(newUser);
        userList.add(newUser2);

        Mockito.when(userService.getAllUsers()).thenReturn(userList);

        assertArrayEquals(userList.toArray(), userController.getAllUsers().toArray());
        Mockito.verify(userService).getAllUsers();
    }

    @Test
    void updateUser_ReturnsFalse_IfUsernameTaken() throws UnsupportedEncodingException {
        User newUser=new User();
        newUser.setUserID(1);
        newUser.setUserName("TophTheGreatest");
        newUser.setPassword("melonlord");
        newUser.setBio("Greatest Earthbender to ever live.");
        newUser.setFirstName("Toph");
        newUser.setLastName("Bei-Fong");
        newUser.setAuthToken("BlahBaBlah");
        newUser.setBusinessName("Toph Greatest EarthBender");
        Date myDate;
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, 8);
        cal.set(Calendar.DATE, 24);
        cal.set(Calendar.YEAR, 2000);
        myDate=cal.getTime();
        newUser.setDob(myDate);
        newUser.setEmail("toph@yahoo.com");
        newUser.setProfileImg("d");
        newUser.setDateCreated(new Date());

        User newUser2= new User();
        newUser2.setUserID(1);
        newUser2.setUserName("TophTheBest");
        newUser2.setPassword("melonlord");
        newUser2.setBio("Greatest Earthbender to ever live.");
        newUser2.setFirstName("Toph");
        newUser2.setLastName("Bei-Fong");
        newUser2.setAuthToken("BlahBaBlah");
        newUser2.setBusinessName("Toph Greatest EarthBender");
        newUser2.setDob(myDate);
        newUser2.setEmail("toph@yahoo.com");
        newUser2.setProfileImg("d");
        newUser2.setDateCreated(new Date());

        Mockito.when(userService.getUserByID(1)).thenReturn(newUser);
        Mockito.when(userService.getUserByUserName("tophthebest")).thenReturn(newUser);

        String token= JwtEncryption.encrypt(newUser2);
        assertFalse(userController.updateUser(token, newUser2));
    }

    @Test
    void updateUser_ReturnsFalse_IfEmailTaken() throws UnsupportedEncodingException {
        User newUser=new User();
        newUser.setUserID(1);
        newUser.setUserName("TophTheGreatest");
        newUser.setPassword("melonlord");
        newUser.setBio("Greatest Earthbender to ever live.");
        newUser.setFirstName("Toph");
        newUser.setLastName("Bei-Fong");
        newUser.setAuthToken("BlahBaBlah");
        newUser.setBusinessName("Toph Greatest EarthBender");
        Date myDate;
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, 8);
        cal.set(Calendar.DATE, 24);
        cal.set(Calendar.YEAR, 2000);
        myDate=cal.getTime();
        newUser.setDob(myDate);
        newUser.setEmail("toph@yahoo.com");
        newUser.setProfileImg("d");
        newUser.setDateCreated(new Date());

        User newUser2= new User();
        newUser2.setUserID(1);
        newUser2.setUserName("TophTheGreatest");
        newUser2.setPassword("melonlord");
        newUser2.setBio("Greatest Earthbender to ever live.");
        newUser2.setFirstName("Toph");
        newUser2.setLastName("Bei-Fong");
        newUser2.setAuthToken("BlahBaBlah");
        newUser2.setBusinessName("Toph Greatest EarthBender");
        newUser2.setDob(myDate);
        newUser2.setEmail("toph1@yahoo.com");
        newUser2.setProfileImg("d");
        newUser2.setDateCreated(new Date());

        Mockito.when(userService.getUserByID(1)).thenReturn(newUser);
        Mockito.when(userService.getUserByEmail("toph1@yahoo.com")).thenReturn(newUser);

        String token= JwtEncryption.encrypt(newUser2);
        assertFalse(userController.updateUser(token, newUser2));
    }


    @Test
    void updateUser_ReturnsTrue_IfUserUpdated() throws UnsupportedEncodingException {
        User newUser=new User();
        newUser.setUserID(1);
        newUser.setUserName("TophTheGreatest");
        newUser.setPassword("melonlord");
        newUser.setBio("Greatest Earthbender to ever live.");
        newUser.setFirstName("Toph");
        newUser.setLastName("Bei-Fong");
        newUser.setAuthToken("BlahBaBlah");
        newUser.setBusinessName("Toph Greatest EarthBender");
        Date myDate;
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, 8);
        cal.set(Calendar.DATE, 24);
        cal.set(Calendar.YEAR, 2000);
        myDate=cal.getTime();
        newUser.setDob(myDate);
        newUser.setEmail("toph@yahoo.com");
        newUser.setProfileImg("d");
        newUser.setDateCreated(new Date());

        User newUser2= new User();
        newUser2.setUserID(1);
        newUser2.setUserName("TophTheBest");
        newUser2.setPassword("melonlord");
        newUser2.setBio("Greatest Earthbender to ever live.");
        newUser2.setFirstName("Toph");
        newUser2.setLastName("Bei-Fong");
        newUser2.setAuthToken("BlahBaBlah");
        newUser2.setBusinessName("Toph Greatest EarthBender");
        newUser2.setDob(myDate);
        newUser2.setEmail("toph1@yahoo.com");
        newUser2.setProfileImg("d");
        newUser2.setDateCreated(new Date());

        Mockito.when(userService.getUserByID(1)).thenReturn(newUser);
        Mockito.when(userService.getUserByUserName("tophthebest")).thenReturn(null);
        Mockito.when(userService.getUserByEmail("toph1@yahoo.com")).thenReturn(null);

        String token= JwtEncryption.encrypt(newUser2);
        assertTrue(userController.updateUser(token, newUser2));
        Mockito.verify(userService).updateUser(newUser2);
    }

//    @Test
//    void updateUser_ReturnsFalse_IfExceptionThrown() throws UnsupportedEncodingException {
//        User newUser=new User();
//        newUser.setUserID(1);
//        newUser.setUserName("TophTheGreatest");
//        newUser.setPassword("melonlord");
//        newUser.setBio("Greatest Earthbender to ever live.");
//        newUser.setFirstName("Toph");
//        newUser.setLastName("Bei-Fong");
//        newUser.setAuthToken("BlahBaBlah");
//        newUser.setBusinessName("Toph Greatest EarthBender");
//        Date myDate;
//        Calendar cal = Calendar.getInstance();
//        cal.set(Calendar.MONTH, 8);
//        cal.set(Calendar.DATE, 24);
//        cal.set(Calendar.YEAR, 2000);
//        myDate=cal.getTime();
//        newUser.setDob(myDate);
//        newUser.setEmail("toph@yahoo.com");
//        newUser.setProfileImg("d");
//        newUser.setDateCreated(new Date());
//
//
//
//        String token= JwtEncryption.encrypt(newUser);
//        assertFalse(userController.updateUser(token, newUser));
//    }
}