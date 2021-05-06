package com.link.controllers;

import com.link.model.CustomResponseMessage;
import com.link.service.S3Service;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class ImageControllerTest {

    ImageController imageController;
    @Mock
    S3Service s3Service;

    @BeforeEach
    void setUp() {
        imageController= new ImageController(s3Service);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void uploadImg_ReturnCRM_WhenImageUploaded() throws IOException {

        byte[] bytea = javax.xml.bind.DatatypeConverter.
                parseHexBinary("e04fd020ea3a6910a2d808002b30309d");

        MultipartFile file= new MockMultipartFile("testFile", bytea);

        String keyName = file.getOriginalFilename();
        String bucketUrl = "https://linksocialnetworkbucket.s3.us-east-2.amazonaws.com/";
        CustomResponseMessage crmExpected=  new CustomResponseMessage(bucketUrl+keyName);

        assertEquals(crmExpected, imageController.uploadImg(file));
    }
}