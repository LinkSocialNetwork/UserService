package com.link.controllers;

import com.link.model.CustomResponseMessage;
import com.link.service.S3Service;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/userservice")
@Getter
@Setter
public class ImageController {

    private S3Service s3Service;

    /**
     * Image endpoint that converts and passes an image file to be stored in an s3 bucket.
     * @param file Image file from HTTP request body (form-data).
     * @return String containing the file name that was uploaded.
     * @throws IOException
     */
    @PostMapping("/image")
    public CustomResponseMessage uploadImg(@RequestParam("file") MultipartFile file) throws IOException {
        String keyName = file.getOriginalFilename();
        String bucketUrl = "https://linksocialnetworkbucket.s3.us-east-2.amazonaws.com/";

        s3Service.uploadFile(keyName, file);

        return new CustomResponseMessage(bucketUrl+keyName);
    }

    @Autowired
    public ImageController(S3Service s3Service) {
        this.s3Service = s3Service;
    }
}
